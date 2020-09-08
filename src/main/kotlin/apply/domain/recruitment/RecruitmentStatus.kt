package apply.domain.recruitment

enum class RecruitmentStatus(val title: String, private val condition: (Boolean, Boolean) -> Boolean) {
    RECRUITABLE("모집 종료", { isAfterEndDateTime, canRecruit -> !isAfterEndDateTime && canRecruit }),
    UNRECRUITABLE("모집 가능", { isAfterEndDateTime, canRecruit -> !isAfterEndDateTime && !canRecruit }),
    ENDED("모집 불가능", { isAfterEndDateTime, _ -> isAfterEndDateTime });

    companion object {
        fun of(isAfterEndDateTime: Boolean, canRecruit: Boolean): RecruitmentStatus {
            return values().first { it.condition(isAfterEndDateTime, canRecruit) }
        }
    }
}
