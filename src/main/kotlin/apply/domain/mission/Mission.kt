package apply.domain.mission

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

    @Column(nullable = false, unique = true)
    val evaluationId: Long,

    @Embedded
    var period: MissionPeriod,

    @Column(nullable = false)
    var submittable: Boolean = false,
    id: Long = 0L
) : BaseEntity(id) {

    val status: MissionStatus
        get() = MissionStatus.of(period, submittable)

    val isProgressing: Boolean
        get() = status == MissionStatus.PROGRESSING

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
