package apply.application

import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class EvaluationRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotBlank
    var description: String = "",

    @field:NotNull
    var recruitmentId: Long = 0L,

    var beforeEvaluationId: Long = 0L,

    @field:NotNull
    @field:Valid
    var evaluationItems: List<EvaluationItemRequest> = emptyList()
)

data class EvaluationItemRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 255)
    var title: String = "",

    @field:NotNull
    @field:Min(0)
    @field:Max(10)
    var maximumScore: Int = 0,

    @field:NotNull
    @field:Min(1)
    @field:Max(10)
    var position: Int = 0,

    @field:NotBlank
    var description: String = ""
)

data class EvaluationResponse(
    val id: Long,
    val title: String,
    val description: String,
    val recruitmentTitle: String,
    val recruitmentId: Long,
    val beforeEvaluationTitle: String = "",
    val beforeEvaluationId: Long = 0L
)
