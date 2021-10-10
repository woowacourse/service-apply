package apply.domain.mission

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@SQLDelete(sql = "update mission set deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
class Mission(
    @Column(nullable = false)
    val title: String,

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

    constructor(
        title: String,
        description: String,
        evaluationId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        submittable: Boolean = false,
        id: Long = 0L
    ) : this(title, description, evaluationId, MissionPeriod(startDateTime, endDateTime), submittable, id)
}
