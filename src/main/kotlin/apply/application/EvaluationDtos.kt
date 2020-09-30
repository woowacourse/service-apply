package apply.application

import apply.domain.evaluationtarget.EvaluationStatus
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
    @field:Max(100)
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

data class GradeEvaluationResponse(
    val note: String,
    val evaluationStatus: EvaluationStatus,
    val evaluationDescription: String,
    val evaluationTitle: String,
    val evaluationItems: List<GradeEvaluationItemResponse>
)

data class GradeEvaluationItemResponse(
    val title: String,
    val description: String,
    val maximumScore: Int,
    val id: Long,
    val score: Int
)

data class GradeEvaluationRequest(
    @field:NotNull
    @field:Valid
    var evaluationAnswers: List<EvaluationAnswerRequest> = emptyList(),

    @field:Size(max = 255)
    var note: String = "",

    @field:NotNull
    var evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING
)

data class EvaluationAnswerRequest(
    @field:NotNull
    @field:Min(0)
    var score: Int = 0,

    @field:NotNull
    var evaluationItemId: Long = 0L
)
