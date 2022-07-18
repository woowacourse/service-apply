package apply.domain.recruitment

import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDateTime

class RecruitmentPeriodTest : StringSpec({
    lateinit var now: LocalDateTime

    beforeEach { now = LocalDateTime.now() }

    "시작 일시와 종료 일시는 동일할 수 있다" {
        assertDoesNotThrow {
            RecruitmentPeriod(now, now)
        }
    }

    "시작 일시는 종료 일시보다 이후일 수 없다" {
        assertThatIllegalArgumentException().isThrownBy {
            RecruitmentPeriod(now, now.minusDays(1L))
        }
    }

    "시작 일시와 종료 일시가 같으면 같은 기간이다" {
        assertThat(RecruitmentPeriod(now, now)).isEqualTo(RecruitmentPeriod(now, now))
    }

    "모집 기간 전" {
        val tomorrow = now.plusDays(1L)
        val period = RecruitmentPeriod(tomorrow, tomorrow)
        assertThat(now.isBefore(period)).isTrue()
    }

    "모집 기간 중" {
        val period = RecruitmentPeriod(now.minusDays(1L), now.plusDays(1L))
        assertThat(now.isBetween(period)).isTrue()
    }

    "모집 기간 후" {
        val yesterday = now.minusDays(1L)
        val period = RecruitmentPeriod(yesterday, yesterday)
        assertThat(now.isAfter(period)).isTrue()
    }
})
