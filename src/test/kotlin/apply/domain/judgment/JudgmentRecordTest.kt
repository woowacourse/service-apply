package apply.domain.judgment

import apply.createJudgmentRecord
import apply.domain.judgment.JudgmentStatus.FAILED
import apply.domain.judgment.JudgmentStatus.STARTED
import apply.domain.judgment.JudgmentStatus.SUCCEEDED
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime.now

class JudgmentRecordTest : StringSpec({
    "자동 채점 기록 생성" {
        val record = createJudgmentRecord()
        assertSoftly(record) {
            result shouldBe JudgmentResult()
            completedDateTime.shouldBeNull()
            completed.shouldBeFalse()
            status shouldBe STARTED
        }
    }

    "자동 채점 기록 생성 실패" {
        listOf(
            JudgmentResult(passCount = 0, totalCount = 0, message = "") to now(),
            JudgmentResult(passCount = 9, totalCount = 10) to null,
            JudgmentResult(message = "message") to null
        ).forAll { (result, completedDateTime) ->
            shouldThrow<IllegalArgumentException> {
                createJudgmentRecord(result = result, completedDateTime = completedDateTime)
            }
        }
    }

    "빈 자동 채점 결과는 적용할 수 없다" {
        val record = createJudgmentRecord()
        shouldThrow<IllegalArgumentException> {
            record.applyResult(JudgmentResult(passCount = 0, totalCount = 0, message = ""))
        }
    }

    "성공한 자동 채점 결과 적용" {
        val record = createJudgmentRecord()
        record.applyResult(JudgmentResult(passCount = 9, totalCount = 10))
        assertSoftly(record) {
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            status shouldBe SUCCEEDED
        }
    }

    "실패한 자동 채점 결과 적용" {
        val record = createJudgmentRecord()
        record.applyResult(JudgmentResult(message = "message"))
        assertSoftly(record) {
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            status shouldBe FAILED
        }
    }

    "자동 채점 기록 시작" {
        val record = createJudgmentRecord(
            result = JudgmentResult(passCount = 9, totalCount = 10),
            completedDateTime = now()
        )
        record.start()
        assertSoftly(record) {
            result shouldBe JudgmentResult()
            completedDateTime.shouldBeNull()
            completed.shouldBeFalse()
            status shouldBe STARTED
        }
    }
})
