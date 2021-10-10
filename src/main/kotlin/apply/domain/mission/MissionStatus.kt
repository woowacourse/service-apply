package apply.domain.mission

import java.time.LocalDateTime

enum class MissionStatus {
    SUBMITTABLE, SUBMITTING, UNSUBMITTABLE, ENDED;

    companion object {
        fun of(period: MissionPeriod, submittable: Boolean): MissionStatus {
            val now = LocalDateTime.now()
            return when {
                now.isBefore(period) -> SUBMITTABLE
                now.isBetween(period) && submittable -> SUBMITTING
                now.isBetween(period) && !submittable -> UNSUBMITTABLE
                now.isAfter(period) -> ENDED
                else -> throw IllegalArgumentException()
            }
        }
    }
}
