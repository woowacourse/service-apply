package apply.domain.mission

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Lob

@SQLDelete(sql = "update mission set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Mission(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    @Lob
    val description: String,

    @Column(nullable = false)
    val evaluationId: Long,

    @Embedded
    var period: MissionPeriod,

    @Column(nullable = false)
    var submittable: Boolean = false,

    @Column(nullable = false)
    var hidden: Boolean = true,

    @Column(nullable = false, columnDefinition = "varchar(255) default 'PUBLIC_PULL_REQUEST'")
    @Enumerated(EnumType.STRING)
    val submissionMethod: SubmissionMethod,
    id: Long = 0L,
) : BaseEntity(id) {
    @Column(nullable = false)
    private var deleted: Boolean = false

    val status: MissionStatus
        get() = MissionStatus.of(period, submittable)

    val isSubmitting: Boolean
        get() = status == MissionStatus.SUBMITTING

    val isDescriptionViewable: Boolean
        get() = !hidden && status in listOf(MissionStatus.SUBMITTING, MissionStatus.UNSUBMITTABLE)

    constructor(
        title: String,
        description: String,
        evaluationId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        submittable: Boolean = false,
        hidden: Boolean = true,
        submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
        id: Long = 0L,
    ) : this(
        title,
        description,
        evaluationId,
        MissionPeriod(startDateTime, endDateTime),
        submittable,
        hidden,
        submissionMethod,
        id
    )
}
