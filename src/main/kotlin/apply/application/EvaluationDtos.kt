package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.recruitment.Recruitment
import apply.domain.recruitmentitem.RecruitmentItem
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class EvaluationData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotBlank
    var description: String = "",

    @field:NotNull
    val recruitmentId: Long = 0L,

    var beforeEvaluationId: Long = 0L,

    var id: Long = 0L
) {
    constructor(evaluation: Evaluation) : this(
        evaluation.title,
        evaluation.description,
        evaluation.recruitmentId,
        evaluation.beforeEvaluationId,
        evaluation.id
    )
}

data class EvaluationFormData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotBlank
    var description: String = "",

    @field:NotNull
    var recruitment: RecruitmentData = RecruitmentData(),

    var beforeEvaluation: EvaluationData = EvaluationData(),

    @field:NotNull
    @field:Valid
    var evaluationItems: List<EvaluationItemData> = emptyList(),
    var id: Long = 0L
) {
    constructor(
        evaluation: Evaluation,
        recruitment: Recruitment,
        recruitmentItems: List<RecruitmentItem>,
        beforeEvaluation: Evaluation?,
        evaluationItems: List<EvaluationItem>
    ) : this(
        title = evaluation.title,
        description = evaluation.description,
        recruitment = RecruitmentData(recruitment, recruitmentItems),
        beforeEvaluation = EvaluationData(
            beforeEvaluation ?: Evaluation(
                title = "이전 평가 없음",
                description = "이전 평가 없음",
                recruitmentId = recruitment.id
            )
        ),
        evaluationItems = evaluationItems.map(::EvaluationItemData),
        id = evaluation.id
    )
}

data class EvaluationItemData(
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
    var description: String = "",
    var id: Long = 0L
) {
    constructor(evaluationItem: EvaluationItem) : this(
        evaluationItem.title,
        evaluationItem.maximumScore,
        evaluationItem.position,
        evaluationItem.description,
        evaluationItem.id
    )
}

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
    val evaluationTarget: EvaluationTargetData,
    val evaluationItems: List<EvaluationItemResponse>
)

data class EvaluationTargetData(
    @field:NotNull
    @field:Valid
    var evaluationItemScores: List<EvaluationItemScoreData> = emptyList(),

    @field:Size(max = 255)
    var note: String = "",

    @field:NotNull
    var evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING
)

data class EvaluationItemScoreData(
    @field:NotNull
    @field:Min(0)
    var score: Int = 0,
    var id: Long = 0L
)
