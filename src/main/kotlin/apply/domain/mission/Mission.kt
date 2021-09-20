package apply.domain.mission

import apply.domain.recruitment.RecruitmentPeriod
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class Mission(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val recruitmentId: Long,

    @Column(nullable = false)
    val evaluationId: Long,

    @Embedded
    var period: RecruitmentPeriod,

    @Column(nullable = false)
    var submittable: Boolean = false,
    id: Long = 0L
) : BaseEntity(id) {
    constructor(
        title: String,
        description: String,
        recruitmentId: Long,
        evaluationId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        submittable: Boolean = false,
        id: Long = 0L
    ) : this(title, description, recruitmentId, evaluationId, RecruitmentPeriod(startDateTime, endDateTime), submittable, id)
}
