package apply.domain.recruitment

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Recruitment(
    @Column(nullable = false)
    var title: String,
    canRecruit: Boolean,

    @Embedded
    var period: RecruitmentPeriod,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
) {
    @Column(nullable = false)
    var canRecruit: Boolean = canRecruit
        private set

    val startDateTime: LocalDateTime
        get() = period.startDateTime

    val endDateTime: LocalDateTime
        get() = period.endDateTime

    val status: RecruitmentStatus
        get() = RecruitmentStatus.of(isPeriodOver = period.isOver, canRecruit = canRecruit)

    constructor(title: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime) : this(
        title,
        false,
        startDateTime,
        endDateTime
    )

    constructor(title: String, canRecruit: Boolean, startDateTime: LocalDateTime, endDateTime: LocalDateTime) : this(
        title,
        canRecruit,
        RecruitmentPeriod(startDateTime, endDateTime)
    )

    fun start() {
        canRecruit = true
    }

    fun stop() {
        canRecruit = false
    }
}
