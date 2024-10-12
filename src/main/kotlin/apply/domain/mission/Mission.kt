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
    val evaluationId: Long,

    @Embedded
    var period: MissionPeriod,

    @Column(nullable = false)
    @Lob
    val description: String,

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

    val status: MissionStatus get() = MissionStatus.of(period, submittable)
    val isSubmitting: Boolean get() = status.submittable
    val isDescriptionViewable: Boolean get() = !hidden && status.viewable

    constructor(
        title: String,
        evaluationId: Long,
        startDateTime: LocalDateTime,
        submissionStartDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        description: String,
        submittable: Boolean,
        hidden: Boolean,
        submissionMethod: SubmissionMethod,
        id: Long = 0L,
    ) : this(
        title,
        evaluationId,
        MissionPeriod(
            startDateTime = startDateTime,
            submissionStartDateTime = submissionStartDateTime,
            endDateTime = endDateTime
        ),
        description,
        submittable,
        hidden,
        submissionMethod,
        id
    )

    fun validateSameEvaluation(evaluationId: Long) {
        require(this.evaluationId == evaluationId) { "과제의 평가는 수정할 수 없습니다." }
    }
}
