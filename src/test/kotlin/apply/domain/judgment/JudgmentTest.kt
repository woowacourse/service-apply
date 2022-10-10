package apply.domain.judgment

import apply.createCommit
import apply.createJudgment
import apply.createJudgmentRecord
import apply.domain.judgment.JudgmentStatus.CANCELLED
import apply.domain.judgment.JudgmentStatus.FAILED
import apply.domain.judgment.JudgmentStatus.STARTED
import apply.domain.judgment.JudgmentStatus.SUCCEEDED
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime.now

class JudgmentTest : StringSpec({
    "자동 채점 시작" {
        val judgment = createJudgment()
        val commit = createCommit()
        judgment.start(commit)
        judgment.lastCommit shouldBe commit
        judgment.lastStatus shouldBe STARTED
    }

    "자동 채점이 완료되지 않고 시작 후 5분이 지나지 않은 경우 자동 채점을 시작할 수 없다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now()))
        )
        shouldThrow<IllegalStateException> {
            judgment.start(createCommit("commit2"))
        }
    }

    "자동 채점이 완료되면 시작 일시에 관계없이 자동 채점을 시작할 수 있다" {
        listOf(SUCCEEDED, FAILED, CANCELLED).forAll { status ->
            val judgment = createJudgment(
                records = listOf(
                    createJudgmentRecord(
                        createCommit("commit1"),
                        JudgmentResult(status = status),
                        completedDateTime = now()
                    )
                )
            )
            val commit = createCommit("commit2")
            shouldNotThrowAny { judgment.start(commit) }
            judgment.lastCommit shouldBe commit
            judgment.lastStatus shouldBe STARTED
        }
    }

    "자동 채점 완료 여부와 상관없이 시작 5분 후에 자동 채점을 시작할 수 있다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now().minusMinutes(5)))
        )
        val commit = createCommit("commit2")
        shouldNotThrowAny { judgment.start(commit) }
        judgment.lastCommit shouldBe commit
        judgment.lastStatus shouldBe STARTED
    }

    "특정 커밋에 대한 결과가 이미 있는 경우 자동 채점 기록의 시작 및 완료 일시만 변경한다" {
        listOf(
            JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
            JudgmentResult(message = "빌드 실패", status = FAILED)
        ).forAll { result ->
            val now = now()
            val commit = createCommit("commit")
            val judgment = createJudgment(
                records = listOf(createJudgmentRecord(commit, result, startedDateTime = now, completedDateTime = now))
            )
            shouldNotThrowAny { judgment.start(commit) }
            judgment.lastCommit shouldBe commit
            judgment.lastStatus shouldBe result.status
            judgment.lastRecord.startedDateTime shouldBe judgment.lastRecord.completedDateTime
        }
    }

    "특정 커밋에 대해 다시 채점해야 하는 경우 자동 채점 기록을 초기화하고 자동 채점을 시작한다" {
        listOf(
            JudgmentResult(status = STARTED) to null,
            JudgmentResult(message = "서버 실패", status = CANCELLED) to now()
        ).forAll { (result, completedDateTime) ->
            val now = now()
            val commit = createCommit("commit")
            val judgment = createJudgment(
                records = listOf(
                    createJudgmentRecord(
                        commit,
                        result,
                        startedDateTime = now.minusMinutes(5),
                        completedDateTime = completedDateTime
                    )
                )
            )
            shouldNotThrowAny { judgment.start(commit) }
            judgment.lastCommit shouldBe commit
            judgment.lastStatus shouldBe STARTED
            judgment.lastRecord.completedDateTime.shouldBeNull()
        }
    }

    "자동 채점 성공" {
        val commit = createCommit()
        val judgment = createJudgment(records = listOf(createJudgmentRecord(commit)))
        judgment.success(commit, passCount = 9, totalCount = 10)
        judgment.lastStatus shouldBe SUCCEEDED
    }

    "자동 채점 실패" {
        val commit = createCommit()
        val judgment = createJudgment(records = listOf(createJudgmentRecord(commit)))
        judgment.fail(commit, "message")
        judgment.lastStatus shouldBe FAILED
    }

    "자동 채점 취소" {
        val commit = createCommit()
        val judgment = createJudgment(records = listOf(createJudgmentRecord(commit)))
        judgment.cancel(commit, "message")
        judgment.lastStatus shouldBe CANCELLED
    }
})
