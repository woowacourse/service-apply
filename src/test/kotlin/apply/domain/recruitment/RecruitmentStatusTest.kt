package apply.domain.recruitment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime

internal class RecruitmentStatusTest {
    @ValueSource(booleans = [true, false])
    @ParameterizedTest
    fun `시작 일시 전이면 모집 예정`(recruitable: Boolean) {
        val tomorrow = LocalDateTime.now().plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = tomorrow, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, recruitable)
        assertThat(status).isEqualTo(RecruitmentStatus.RECRUITABLE)
    }

    @Test
    fun `모집 기간 사이이면서 모집이 가능한 경우 모집 중`() {
        val yesterday = LocalDateTime.now().minusDays(1L)
        val tomorrow = LocalDateTime.now().plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, true)
        assertThat(status).isEqualTo(RecruitmentStatus.RECRUITING)
    }

    @Test
    fun `모집 기간 사이이면서 모집이 불가능한 경우 모집 중지`() {
        val yesterday = LocalDateTime.now().minusDays(1L)
        val tomorrow = LocalDateTime.now().plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, false)
        assertThat(status).isEqualTo(RecruitmentStatus.UNRECRUITABLE)
    }

    @ValueSource(booleans = [true, false])
    @ParameterizedTest
    fun `종료 일시가 지나면 모집 종료`(recruitable: Boolean) {
        val yesterday = LocalDateTime.now().minusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = yesterday)
        val status = RecruitmentStatus.of(period, recruitable)
        assertThat(status).isEqualTo(RecruitmentStatus.ENDED)
    }
}
