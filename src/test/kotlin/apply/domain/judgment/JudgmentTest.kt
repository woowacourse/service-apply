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
import io.kotest.matchers.nulls.shouldNotBeNull
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

    "마지막 자동 채점 시작 후 5분이 지나지 않은 경우 채점을 시작할 수 없다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now()))
        )
        shouldThrow<IllegalStateException> {
            judgment.start(createCommit("commit2"))
        }
    }

    "마지막 자동 채점이 완료되었다면 시작 시각과 관계 없이 채점을 시작할 수 있다" {
        listOf(SUCCEEDED, FAILED, CANCELLED).forAll { status ->
            val now = now()
            val judgment = createJudgment(
                records = listOf(
                    createJudgmentRecord(
                        createCommit("commit1"),
                        JudgmentResult(passCount = 9, totalCount = 10, status = status),
                        startedDateTime = now.minusMinutes(2),
                        completedDateTime = now.minusMinutes(1)
                    )
                )
            )
            val commit = createCommit("commit2")
            shouldNotThrowAny { judgment.start(commit) }
            judgment.lastCommit shouldBe commit
            judgment.lastStatus shouldBe STARTED
        }
    }

    "마지막 자동 채점의 완료 여부와 관계없이 채점 시작 이후 5분이 지나면 채점을 시작할 수 있다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now().minusMinutes(5)))
        )
        val commit = createCommit("commit2")
        shouldNotThrowAny { judgment.start(commit) }
        judgment.lastCommit shouldBe commit
        judgment.lastStatus shouldBe STARTED
    }

    "마지막 자동 채점이 갱신 가능하다면 채점 시작 시 이전 결과의 생성과 완료 일시만 변경한다" {
        listOf(
            JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
            JudgmentResult(message = "빌드 실패", status = FAILED)
        ).forAll { judgmentResult ->
            val now = now()
            val commit = createCommit("commit")
            val judgment = createJudgment(
                records = listOf(
                    createJudgmentRecord(
                        commit,
                        judgmentResult,
                        startedDateTime = now.minusMinutes(10),
                        completedDateTime = now.minusMinutes(9)
                    )
                )
            )
            shouldNotThrowAny { judgment.start(commit) }
            judgment.lastCommit shouldBe commit
            judgment.lastStatus shouldBe judgmentResult.status
            judgment.lastRecord.completedDateTime.shouldNotBeNull()
        }
    }

    "마지막 자동 채점이 갱신 불가능하다면 채점 기록을 초기화하고 자동 채점을 시작한다" {
        listOf(
            JudgmentResult(status = STARTED) to null,
            JudgmentResult(message = "서버 실패", status = CANCELLED) to now().minusMinutes(10)
        ).forAll { (judgmentResult, completedDateTime) ->
            val now = now()
            val commit = createCommit("commit")
            val judgment = createJudgment(
                records = listOf(
                    createJudgmentRecord(
                        commit,
                        judgmentResult,
                        startedDateTime = now.minusMinutes(10),
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
        judgment.success(commit, 9, 10)
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
