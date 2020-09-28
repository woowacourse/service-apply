package apply.domain.recruitment

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

fun LocalDateTime.isBefore(period: RecruitmentPeriod): Boolean = this < period.startDateTime

fun LocalDateTime.isAfter(period: RecruitmentPeriod): Boolean = this > period.endDateTime

fun LocalDateTime.isBetween(period: RecruitmentPeriod): Boolean = period.contains(this)

@Embeddable
data class RecruitmentPeriod(
    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime
) {
    fun contains(value: LocalDateTime): Boolean = (startDateTime..endDateTime).contains(value)

    init {
        require(endDateTime >= startDateTime)
    }
}
