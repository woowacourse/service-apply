package apply.application

import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class MissionData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotNull
    var recruitment: RecruitmentSelectData = RecruitmentSelectData(),

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

data class MissionResponse(
    val id: Long,
    val title: String,
    val description: String,
    val recruitmentTitle: String,
    val recruitmentId: Long,
    val evaluationTitle: String,
    val evaluationId: Long,
    val submittable: Boolean,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
)
