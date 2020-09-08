package apply.domain.recruitment

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class RecruitmentPeriod(
    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime
) {
    init {
        require(endDateTime >= startDateTime)
    }

    fun isIn(dateTime: LocalDateTime): Boolean {
        return startDateTime >= dateTime && dateTime <= endDateTime
    }
}
