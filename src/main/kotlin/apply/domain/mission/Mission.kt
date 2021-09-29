package apply.domain.mission

import apply.application.MissionData
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@SQLDelete(sql = "update mission set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Mission(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val evaluationId: Long,

    @Embedded
    var period: MissionPeriod,

    @Column(nullable = false)
    var submittable: Boolean = false,
    id: Long = 0L
) : BaseEntity(id) {
    @Column(nullable = false)
    private var deleted: Boolean = false

    val status: MissionStatus
        get() = MissionStatus.of(period, submittable)

    val isSubmitting: Boolean
        get() = status == MissionStatus.SUBMITTING

    constructor(
        title: String,
        description: String,
        evaluationId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        submittable: Boolean = false,
        id: Long = 0L
    ) : this(title, description, evaluationId, MissionPeriod(startDateTime, endDateTime), submittable, id)

    fun update(request: MissionData) {
        this.title = request.title
        this.period.startDateTime = request.startDateTime
        this.period.endDateTime = request.endDateTime
        this.submittable = request.submittable
    }
}
