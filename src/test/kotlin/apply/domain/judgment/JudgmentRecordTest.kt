package apply.domain.judgment

import apply.createJudgmentRecord
import apply.domain.judgment.JudgmentStatus.CANCELLED
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
    "시작 자동 채점 기록 생성" {
        val record = createJudgmentRecord(result = JudgmentResult())
        assertSoftly(record) {
            result shouldBe JudgmentResult()
            completedDateTime.shouldBeNull()
            completed.shouldBeFalse()
            touchable.shouldBeFalse()
            status shouldBe STARTED
        }
    }

    "성공 자동 채점 기록 생성" {
        val record = createJudgmentRecord(
            result = JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
            completedDateTime = now()
        )
        assertSoftly(record) {
            result shouldBe JudgmentResult(9, 10, status = SUCCEEDED)
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            touchable.shouldBeTrue()
            status shouldBe SUCCEEDED
        }
    }

    "실패 자동 채점 기록 생성" {
        val record = createJudgmentRecord(
            result = JudgmentResult(message = "빌드 실패", status = FAILED),
            completedDateTime = now()
        )
        assertSoftly(record) {
            result shouldBe JudgmentResult(message = "빌드 실패", status = FAILED)
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            touchable.shouldBeTrue()
            status shouldBe FAILED
        }
    }

    "취소 자동 채점 기록 생성" {
        val record = createJudgmentRecord(
            result = JudgmentResult(message = "서버 실패", status = CANCELLED),
            completedDateTime = now()
        )
        assertSoftly(record) {
            result shouldBe JudgmentResult(message = "서버 실패", status = CANCELLED)
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            touchable.shouldBeFalse()
            status shouldBe CANCELLED
        }
    }

    "자동 채점 기록 생성 실패" {
        listOf(
            JudgmentResult(passCount = 0, totalCount = 0, message = "", status = STARTED) to now(),
            JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED) to null,
            JudgmentResult(message = "빌드 실패", status = FAILED) to null,
            JudgmentResult(message = "서버 실패", status = CANCELLED) to null
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
        record.applyResult(JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED))
        assertSoftly(record) {
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            touchable.shouldBeTrue()
            status shouldBe SUCCEEDED
        }
    }

    "실패한 자동 채점 결과 적용" {
        val record = createJudgmentRecord()
        record.applyResult(JudgmentResult(message = "message", status = FAILED))
        assertSoftly(record) {
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            touchable.shouldBeTrue()
            status shouldBe FAILED
        }
    }

    "취소된 자동 채점 결과 적용" {
        val record = createJudgmentRecord()
        record.applyResult(JudgmentResult(message = "message", status = CANCELLED))
        assertSoftly(record) {
            completedDateTime.shouldNotBeNull()
            completed.shouldBeTrue()
            touchable.shouldBeFalse()
            status shouldBe CANCELLED
        }
    }

    "자동 채점 기록 시작" {
        val record = createJudgmentRecord(
            result = JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
            completedDateTime = now()
        )
        record.start()
        assertSoftly(record) {
            result shouldBe JudgmentResult(status = STARTED)
            completedDateTime.shouldBeNull()
            completed.shouldBeFalse()
            status shouldBe STARTED
        }
    }
})
