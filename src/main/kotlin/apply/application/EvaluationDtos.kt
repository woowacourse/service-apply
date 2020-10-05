package apply.application

import apply.domain.evaluationItem.EvaluationItem
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

data class EvaluationItemResponse(
    val title: String,
    val description: String,
    val maximumScore: Int,
    val position: Int = 0,
    val evaluationId: Long,
    val id: Long = 0L
) {
    constructor(evaluationItem: EvaluationItem) : this(
        evaluationItem.title,
        evaluationItem.description,
        evaluationItem.maximumScore,
        evaluationItem.position,
        evaluationItem.evaluationId,
        evaluationItem.id
    )
}

data class GradeEvaluationResponse(
    val title: String,
    val description: String,
    val evaluationTargetData: EvaluationTargetData,
    val evaluationItems: List<EvaluationItemResponse>
)

data class EvaluationTargetData(
    @field:NotNull
    @field:Valid
    var evaluationAnswersData: List<EvaluationAnswerData> = emptyList(),

    @field:Size(max = 255)
    var note: String = "",

    @field:NotNull
    var evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING
)

data class EvaluationAnswerData(
    @field:NotNull
    @field:Min(0)
    var score: Int = 0,
    var id: Long = 0L
)
