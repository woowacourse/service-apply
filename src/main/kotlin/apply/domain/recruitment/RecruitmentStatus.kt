package apply.domain.recruitment

import java.time.LocalDateTime

enum class RecruitmentStatus {
    RECRUITABLE, RECRUITING, UNRECRUITABLE, ENDED;

    companion object {
        fun of(period: RecruitmentPeriod, canRecruit: Boolean): RecruitmentStatus {
            val now = LocalDateTime.now()
            return when {
                now.isBefore(period) -> RECRUITABLE
                now.isBetween(period) && canRecruit -> RECRUITING
                now.isBetween(period) && !canRecruit -> UNRECRUITABLE
                now.isAfter(period) -> ENDED
                else -> throw IllegalArgumentException()
            }
        }
    }
}
