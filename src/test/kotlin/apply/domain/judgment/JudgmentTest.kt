package apply.domain.judgment

import apply.createCommit
import apply.createJudgment
import apply.createJudgmentItem
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
    val judgmentItem = createJudgmentItem()
    "자동 채점 시작" {
        val judgment = createJudgment()
        val commit = createCommit()
        judgment.start(commit, judgmentItem)
        judgment.lastCommit shouldBe commit
        judgment.lastStatus shouldBe STARTED
    }

    "마지막 자동 채점 기록 이후 5분이 지나지 않은 경우 예외가 발생한다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now()))
        )
        shouldThrow<IllegalStateException> {
            judgment.start(createCommit("commit2"), judgmentItem)
        }
    }

    "마지막 자동 채점 기록 이후 5분이 지나야 시작할 수 있다" {
        val judgment = createJudgment(
            records = listOf(createJudgmentRecord(createCommit("commit1"), startedDateTime = now().minusMinutes(5)))
        )
        val commit = createCommit("commit2")
        shouldNotThrowAny { judgment.start(commit, judgmentItem) }
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
