package study

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeZero
import io.kotest.matchers.ints.shouldNotBeZero
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.MICROS
import java.time.temporal.ChronoUnit.MILLIS
import java.time.temporal.ChronoUnit.SECONDS

class LocalDateTimeTest : StringSpec({
    val now = LocalDateTime.of(2023, 8, 30, 0, 0, 0, 123_456_789)

    "운영 체제에 따라 시간의 정밀도가 다를 수 있다" {
        assertSoftly(now.withNano(123_456)) {
            milliseconds.shouldBeZero()
            microseconds shouldBe 123
        }
    }

    "지정된 시간 단위보다 작은 단위는 잘린다" {
        assertSoftly(now.truncatedTo(MICROS)) {
            milliseconds.shouldNotBeZero()
            microseconds.shouldNotBeZero()
        }
        assertSoftly(now.truncatedTo(MILLIS)) {
            milliseconds.shouldNotBeZero()
            microseconds.shouldBeZero()
        }
        assertSoftly(now.truncatedTo(SECONDS)) {
            milliseconds.shouldBeZero()
            microseconds.shouldBeZero()
        }
    }

    "지정된 시간 단위의 값만 변경한다" {
        assertSoftly(now.withNano(0)) {
            milliseconds.shouldBeZero()
            microseconds.shouldBeZero()
        }
        assertSoftly(now.withSecond(0)) {
            milliseconds.shouldNotBeZero()
            microseconds.shouldNotBeZero()
        }
    }
})

private val LocalDateTime.milliseconds: Int get() = nano / 1_000_000
private val LocalDateTime.microseconds: Int get() = nano / 1_000 % 1_000
