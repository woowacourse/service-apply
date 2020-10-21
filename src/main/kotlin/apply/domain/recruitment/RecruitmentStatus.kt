package apply.domain.recruitment

import java.time.LocalDateTime

enum class RecruitmentStatus {
    RECRUITABLE, RECRUITING, UNRECRUITABLE, ENDED;

    companion object {
        fun of(period: RecruitmentPeriod, recruitable: Boolean): RecruitmentStatus {
            val now = LocalDateTime.now()
            return when {
                now.isBefore(period) -> RECRUITABLE
                now.isBetween(period) && recruitable -> RECRUITING
                now.isBetween(period) && !recruitable -> UNRECRUITABLE
                now.isAfter(period) -> ENDED
                else -> throw IllegalArgumentException()
            }
        }
    }
}
