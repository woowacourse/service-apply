package apply.domain.recruitment

enum class RecruitmentStatus(val title: String, private val condition: (Boolean, Boolean) -> Boolean) {
    RECRUITABLE("모집 가능", { isPeriodOver, canRecruit -> !isPeriodOver && canRecruit }),
    UNRECRUITABLE("모집 불가능", { isPeriodOver, canRecruit -> !isPeriodOver && !canRecruit }),
    ENDED("모집 종료", { isPeriodOver, _ -> isPeriodOver });

    companion object {
        fun of(isPeriodOver: Boolean, canRecruit: Boolean): RecruitmentStatus {
            return values().first { it.condition(isPeriodOver, canRecruit) }
        }
    }
}
