package apply.domain.judgment

import apply.createCommit
import apply.createJudgment
import apply.createJudgmentRecord
import apply.domain.judgment.JudgmentStatus.FAILED
import apply.domain.judgment.JudgmentStatus.STARTED
import apply.domain.judgment.JudgmentStatus.SUCCEEDED
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
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

    "마지막 자동 채점이 성공이면 시작 시각과 관계 없이 채점을 시작할 수 있다" {
        val now = now()
        val judgment = createJudgment(
            records = listOf(
                createJudgmentRecord(
                    createCommit("commit1"),
                    JudgmentResult(passCount = 9, totalCount = 10),
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

    "마지막 자동 채점이 실패이면 시작 시각과 관계 없이 채점을 시작할 수 있다" {
        val now = now()
        val judgment = createJudgment(
            records = listOf(
                createJudgmentRecord(
                    createCommit("commit1"),
                    JudgmentResult(message = "빌드 실패"),
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

    "마지막 자동 채점의 상태와 상관없이 채점 시작 이후 5분이 지나면 채점을 시작할 수 있다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now().minusMinutes(5)))
        )
        val commit = createCommit("commit2")
        shouldNotThrowAny { judgment.start(commit) }
        judgment.lastCommit shouldBe commit
        judgment.lastStatus shouldBe STARTED
    }

    "자동 채점 성공" {
        val commit = createCommit()
        val judgment = createJudgment(records = listOf(createJudgmentRecord(commit)))
        judgment.success(commit, JudgmentResult(passCount = 9, totalCount = 10))
        judgment.lastStatus shouldBe SUCCEEDED
    }

    "자동 채점 실패" {
        val commit = createCommit()
        val judgment = createJudgment(records = listOf(createJudgmentRecord(commit)))
        judgment.fail(commit, "message")
        judgment.lastStatus shouldBe FAILED
    }
})
