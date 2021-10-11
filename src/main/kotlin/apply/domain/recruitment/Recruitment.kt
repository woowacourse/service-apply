package apply.domain.recruitment

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@SQLDelete(sql = "update recruitment set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Recruitment(
    @Column(nullable = false)
    var title: String,

    @Embedded
    var period: RecruitmentPeriod,

    @Column(nullable = false, updatable = false)
    val termId: Long = 0L,
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

    @Column(nullable = false)
    private var deleted: Boolean = false

    val status: RecruitmentStatus
        get() = RecruitmentStatus.of(period, recruitable)

    val isEnded: Boolean
        get() = status == RecruitmentStatus.ENDED

    val isRecruiting: Boolean
        get() = status == RecruitmentStatus.RECRUITING

    val single: Boolean
        get() = termId == 0L

    constructor(
        title: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        termId: Long = 0L,
        recruitable: Boolean = false,
        hidden: Boolean = true,
        id: Long = 0L
    ) : this(title, RecruitmentPeriod(startDateTime, endDateTime), termId, recruitable, hidden, id)
}
