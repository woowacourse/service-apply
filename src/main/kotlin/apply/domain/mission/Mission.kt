package apply.domain.mission

import apply.domain.recruitment.RecruitmentPeriod
import support.domain.BaseEntity
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
) : BaseEntity(id)
