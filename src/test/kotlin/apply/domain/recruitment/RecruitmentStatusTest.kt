package apply.domain.recruitment

import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class RecruitmentStatusTest : StringSpec({
    "시작 일시 전이면 모집 예정" {
        listOf(true, false).forAll { recruitable ->
            val tomorrow = LocalDateTime.now().plusDays(1L)
            val period = RecruitmentPeriod(startDateTime = tomorrow, endDateTime = tomorrow)
            val status = RecruitmentStatus.of(period, recruitable)
            status shouldBe RecruitmentStatus.RECRUITABLE
        }
    }

    "모집 기간 사이이면서 모집이 가능한 경우 모집 중" {
        val yesterday = LocalDateTime.now().minusDays(1L)
        val tomorrow = LocalDateTime.now().plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, true)
        status shouldBe RecruitmentStatus.RECRUITING
    }

    "모집 기간 사이이면서 모집이 불가능한 경우 모집 중지" {
        val yesterday = LocalDateTime.now().minusDays(1L)
        val tomorrow = LocalDateTime.now().plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, false)
        status shouldBe RecruitmentStatus.UNRECRUITABLE
    }

    "종료 일시가 지나면 모집 종료" {
        listOf(true, false).forAll { recruitable ->
            val yesterday = LocalDateTime.now().minusDays(1L)
            val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = yesterday)
            val status = RecruitmentStatus.of(period, recruitable)
            status shouldBe RecruitmentStatus.ENDED
        }
    }
})
