package apply.domain.recruitment

import apply.domain.recruitment.RecruitmentStatus.ENDED
import apply.domain.recruitment.RecruitmentStatus.RECRUITABLE
import apply.domain.recruitment.RecruitmentStatus.RECRUITING
import apply.domain.recruitment.RecruitmentStatus.UNRECRUITABLE
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class RecruitmentStatusTest : StringSpec({
    val now: LocalDateTime = now()

    "시작 일시 전이면 모집 예정" {
        listOf(true, false).forAll { recruitable ->
            val tomorrow = now.plusDays(1L)
            val period = RecruitmentPeriod(startDateTime = tomorrow, endDateTime = tomorrow)
            val status = RecruitmentStatus.of(period, recruitable)
            status shouldBe RECRUITABLE
        }
    }

    "모집 기간 사이이면서 모집이 가능한 경우 모집 중" {
        val yesterday = now.minusDays(1L)
        val tomorrow = now.plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, true)
        status shouldBe RECRUITING
    }

    "모집 기간 사이이면서 모집이 불가능한 경우 모집 중지" {
        val yesterday = now.minusDays(1L)
        val tomorrow = now.plusDays(1L)
        val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = tomorrow)
        val status = RecruitmentStatus.of(period, false)
        status shouldBe UNRECRUITABLE
    }

    "종료 일시가 지나면 모집 종료" {
        listOf(true, false).forAll { recruitable ->
            val yesterday = now.minusDays(1L)
            val period = RecruitmentPeriod(startDateTime = yesterday, endDateTime = yesterday)
            val status = RecruitmentStatus.of(period, recruitable)
            status shouldBe ENDED
        }
    }
})
