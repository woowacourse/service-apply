package apply.domain.mission

import java.time.LocalDateTime

enum class MissionStatus(
    val label: String,
    val viewable: Boolean,
    val submittable: Boolean,
) {
    PREPARED("준비 중", false, false),
    IN_PROGRESS("진행 중", true, false),
    SUBMITTING("제출 중", true, true),
    UNSUBMITTABLE("제출 중지", true, false),
    ENDED("제출 종료", false, false);

    companion object {
        fun of(period: MissionPeriod, submittable: Boolean): MissionStatus {
            val now = LocalDateTime.now()
            val withinSubmissionPeriod = now in period.toSubmissionPeriod()
            return when {
                now < period.startDateTime -> PREPARED
                now < period.submissionStartDateTime -> IN_PROGRESS
                withinSubmissionPeriod && submittable -> SUBMITTING
                withinSubmissionPeriod && !submittable -> UNSUBMITTABLE
                now > period.endDateTime -> ENDED
                else -> throw IllegalArgumentException()
            }
        }
    }
}
