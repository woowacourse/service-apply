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

    @Column(nullable = false)
    var canRecruit: Boolean,

    @Embedded
    var period: RecruitmentPeriod,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    val startDateTime: LocalDateTime
        get() = period.startDateTime

    val endDateTime: LocalDateTime
        get() = period.endDateTime

    val status: RecruitmentStatus
        get() = RecruitmentStatus(canRecruit, period)

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
}
