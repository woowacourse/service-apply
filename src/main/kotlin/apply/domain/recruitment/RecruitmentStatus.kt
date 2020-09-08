package apply.domain.recruitment

import java.time.LocalDateTime

enum class RecruitmentStatus {
    RECRUITABLE,
    UNRECRUITABLE,
    ENDED,
}

fun RecruitmentStatus(canRecruit: Boolean, period: RecruitmentPeriod): RecruitmentStatus {
    return when {
        !canRecruit -> RecruitmentStatus.UNRECRUITABLE
        period.isIn(LocalDateTime.now()) -> RecruitmentStatus.RECRUITABLE
        else -> RecruitmentStatus.ENDED
    }
}
