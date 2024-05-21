package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluationitem.EvaluationItem
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.member.Member
import apply.domain.recruitment.Recruitment
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

private const val NO_BEFORE_EVALUATION: String = "이전 평가 없음"

data class EvaluationSelectData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",
    var id: Long = 0L
) {
    constructor(evaluation: Evaluation) : this(
        evaluation.title,
        evaluation.id
    )

    companion object {
        fun noBefore(): EvaluationSelectData = EvaluationSelectData(NO_BEFORE_EVALUATION)
    }
}

data class RecruitmentSelectData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",
    var id: Long = 0L
) {
    constructor(recruitment: Recruitment) : this(
        recruitment.title,
        recruitment.id
    )
}

data class EvaluationData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotBlank
    var description: String = "",

    @field:NotNull
    var recruitment: RecruitmentSelectData = RecruitmentSelectData(),
    var beforeEvaluation: EvaluationSelectData = EvaluationSelectData(),

    @field:NotNull
    @field:Valid
    var evaluationItems: List<EvaluationItemData> = emptyList(),
    var id: Long = 0L
) {
    constructor(
        evaluation: Evaluation,
        recruitment: Recruitment,
        beforeEvaluation: Evaluation?,
        evaluationItems: List<EvaluationItem>
    ) : this(
        title = evaluation.title,
        description = evaluation.description,
        recruitment = RecruitmentSelectData(recruitment),
        beforeEvaluation = beforeEvaluation?.let(::EvaluationSelectData) ?: EvaluationSelectData.noBefore(),
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
    val recruitmentId: Long,
    val beforeEvaluationId: Long
) {
    constructor(evaluation: Evaluation) : this(
        evaluation.id,
        evaluation.title,
        evaluation.description,
        evaluation.recruitmentId,
        evaluation.beforeEvaluationId
    )
}

data class EvaluationGridResponse(
    val id: Long,
    val title: String,
    val recruitmentTitle: String,
    val beforeEvaluationTitle: String
) {
    constructor(evaluation: Evaluation, recruitment: Recruitment, beforeEvaluation: Evaluation?) : this(
        evaluation.id,
        evaluation.title,
        recruitment.title,
        beforeEvaluation?.title ?: NO_BEFORE_EVALUATION
    )
}

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

data class MailTargetResponse(
    val email: String,
    val name: String? = null,
    val id: Long,
) {
    constructor(memberResponse: MemberResponse) : this(memberResponse.email, memberResponse.name, memberResponse.id)
    constructor(member: Member) : this(member.email, member.name, member.id)
}

data class EvaluationItemScoreData(
    @field:NotNull
    @field:Min(0)
    var score: Int = 0,
    var id: Long = 0L
)
