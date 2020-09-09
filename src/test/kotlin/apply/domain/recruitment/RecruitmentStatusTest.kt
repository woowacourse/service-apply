package apply.domain.recruitment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RecruitmentStatusTest {
    @Test
    fun `종료 일시가 지나면 모집 종료`() {
        val status = RecruitmentStatus.of(isPeriodOver = true, canRecruit = true)
        assertThat(status).isEqualTo(RecruitmentStatus.ENDED)
    }

    @Test
    fun `종료 일시가 지나지 않고 모집 중이면 모집 가능`() {
        val status = RecruitmentStatus.of(isPeriodOver = false, canRecruit = true)
        assertThat(status).isEqualTo(RecruitmentStatus.RECRUITABLE)
    }

    @Test
    fun `종료 일시가 지나지 않고 모집 중이지 않으면 모집 불가능`() {
        val status = RecruitmentStatus.of(isPeriodOver = false, canRecruit = false)
        assertThat(status).isEqualTo(RecruitmentStatus.UNRECRUITABLE)
    }
}
