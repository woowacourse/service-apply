package apply.domain.recruitment

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class Recruitment(
    @Column(nullable = false)
    var title: String,

    var term: Long?,

    @Embedded
    var period: RecruitmentPeriod,
    recruitable: Boolean = false,
    hidden: Boolean = true,
    id: Long
) : BaseEntity(id) {
    @Column(nullable = false)
    var recruitable: Boolean = recruitable
        private set

    @Column(nullable = false)
    var hidden: Boolean = hidden
        private set

    val startDateTime: LocalDateTime
        get() = period.startDateTime

    val endDateTime: LocalDateTime
        get() = period.endDateTime

    val status: RecruitmentStatus
        get() = RecruitmentStatus.of(period, recruitable)

    val isEnded: Boolean
        get() = status == RecruitmentStatus.ENDED

    val isRecruiting: Boolean
        get() = status == RecruitmentStatus.RECRUITING

    constructor(
        title: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        recruitable: Boolean = false,
        hidden: Boolean = true,
        id: Long = 0L
    ) : this(title, null, RecruitmentPeriod(startDateTime, endDateTime), recruitable, hidden, id)

    constructor(
        title: String,
        term: Long?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        recruitable: Boolean = false,
        hidden: Boolean = true,
        id: Long = 0L
    ) : this(title, term, RecruitmentPeriod(startDateTime, endDateTime), recruitable, hidden, id)
}
