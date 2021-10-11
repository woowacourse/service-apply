package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.mission.Mission
import apply.domain.mission.MissionStatus
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class MissionData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotNull
    var evaluation: EvaluationSelectData = EvaluationSelectData(),

    @field:NotNull
    var startDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotNull
    var endDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotBlank
    var description: String = "",

    @field:NotNull
    var submittable: Boolean = false,

    var id: Long = 0L
)

data class MissionAndEvaluationResponse(
    val id: Long,
    val title: String,
    val description: String,
    val evaluationTitle: String,
    val evaluationId: Long,
    val submittable: Boolean,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: MissionStatus
) {
    constructor(mission: Mission, evaluation: Evaluation) : this(
        mission.id,
        mission.title,
        mission.description,
        evaluation.title,
        evaluation.id,
        mission.submittable,
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.status
    )
}

data class MissionResponse(
    val id: Long,
    val title: String,
    val description: String,
    val submittable: Boolean,
    val submitted: Boolean,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: MissionStatus
)

