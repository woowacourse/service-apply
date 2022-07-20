package apply.domain.recruitment

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class RecruitmentPeriodTest : StringSpec({
    val now: LocalDateTime = now()

    "시작 일시와 종료 일시는 동일할 수 있다" {
        shouldNotThrowAny { RecruitmentPeriod(startDateTime = now, endDateTime = now) }
    }

    "시작 일시는 종료 일시보다 이후일 수 없다" {
        shouldThrow<IllegalArgumentException> {
            RecruitmentPeriod(startDateTime = now, endDateTime = now.minusDays(1L))
        }
    }

    "시작 일시와 종료 일시가 같으면 같은 기간이다" {
        val tomorrow = now.plusDays(1L)
        RecruitmentPeriod(startDateTime = now, endDateTime = tomorrow) shouldBe RecruitmentPeriod(now, tomorrow)
    }

    "모집 기간 전" {
        val tomorrow = now.plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = tomorrow, endDateTime = tomorrow)
        now.isBefore(period).shouldBeTrue()
    }

    "모집 기간 중" {
        val period = RecruitmentPeriod(startDateTime = now.minusDays(1L), endDateTime = now.plusDays(1L))
        now.isBetween(period).shouldBeTrue()
    }

    "모집 기간 후" {
        val yesterday = now.minusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = yesterday)
        now.isAfter(period).shouldBeTrue()
    }
})
