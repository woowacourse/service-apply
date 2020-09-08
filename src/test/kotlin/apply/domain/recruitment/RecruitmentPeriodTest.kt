package apply.domain.recruitment

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDateTime

internal class RecruitmentPeriodTest {
    @Test
    fun `시작 일시와 종료 일시는 동일할 수 있다`() {
        val now = LocalDateTime.now()
        assertDoesNotThrow {
            RecruitmentPeriod(now, now)
        }
    }

    @Test
    fun `시작 일시는 종료 일시보다 이후일 수 없다`() {
        val now = LocalDateTime.now()
        assertThatIllegalArgumentException().isThrownBy {
            RecruitmentPeriod(now, now.minusDays(1))
        }
    }

    @Test
    fun `시작 일시와 종료 일시가 같으면 같은 기간이다`() {
        val now = LocalDateTime.now()
        assertThat(RecruitmentPeriod(now, now)).isEqualTo(RecruitmentPeriod(now, now))
    }
}
