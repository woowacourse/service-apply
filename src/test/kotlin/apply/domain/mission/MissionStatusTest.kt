package apply.domain.mission

import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.types.shouldBeSameInstanceAs
import java.time.LocalDateTime

class MissionStatusTest : StringSpec({
    val today = LocalDateTime.now()
    val yesterday = today.minusDays(1L)
    val tomorrow = today.plusDays(1L)

    "현재 일시가 시작 일시 전이면 제출 여부와 관계없이 준비 중이다" {
        listOf(true, false).forAll { submittable ->
            val period = MissionPeriod(
                startDateTime = tomorrow,
                submissionStartDateTime = tomorrow,
                endDateTime = tomorrow
            )
            val actual = MissionStatus.of(period, submittable)
            actual shouldBeSameInstanceAs MissionStatus.PREPARED
        }
    }

    "현재 일시가 시작 일시 이후이지만 제출 시작 일시 전이면 진행 중이다" {
        listOf(true, false).forAll { submittable ->
            val period = MissionPeriod(
                startDateTime = yesterday,
                submissionStartDateTime = tomorrow,
                endDateTime = tomorrow
            )
            val actual = MissionStatus.of(period, submittable)
            actual shouldBeSameInstanceAs MissionStatus.IN_PROGRESS
        }
    }

    "현재 일시가 제출 시작 일시 이후이고 제출이 허용되면 제출 중이다" {
        val period = MissionPeriod(
            startDateTime = yesterday,
            submissionStartDateTime = yesterday,
            endDateTime = tomorrow
        )
        val actual = MissionStatus.of(period, true)
        actual shouldBeSameInstanceAs MissionStatus.SUBMITTING
    }

    "현재 일시가 제출 시작 일시 이후라도 제출이 허용되지 않으면 제출 중지이다" {
        val period = MissionPeriod(
            startDateTime = yesterday,
            submissionStartDateTime = yesterday,
            endDateTime = tomorrow
        )
        val actual = MissionStatus.of(period, false)
        actual shouldBeSameInstanceAs MissionStatus.UNSUBMITTABLE
    }

    "현재 일시가 종료 일시 후면 제출 종료이다" {
        listOf(true, false).forAll { submittable ->
            val period = MissionPeriod(
                startDateTime = yesterday,
                submissionStartDateTime = yesterday,
                endDateTime = yesterday
            )
            val actual = MissionStatus.of(period, submittable)
            actual shouldBeSameInstanceAs MissionStatus.ENDED
        }
    }
})
