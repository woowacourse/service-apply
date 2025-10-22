package apply.application

import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.member.Member

data class EvaluationTargetResponse(
    val id: Long,
    val memberId: Long,
    val name: String?,
    val email: String?,
    val totalScore: Int,
    val evaluationStatus: EvaluationStatus,
    val administratorId: Long,
    val note: String,
    val answers: List<EvaluationAnswerResponse>,
) {
    constructor(evaluationTarget: EvaluationTarget, member: Member) : this(
        evaluationTarget.id,
        member.id,
        member.name,
        member.email,
        evaluationTarget.evaluationAnswers.countTotalScore(),
        evaluationTarget.evaluationStatus,
        evaluationTarget.administratorId ?: 0L,
        evaluationTarget.note,
        evaluationTarget.evaluationAnswers.answers.map(::EvaluationAnswerResponse),
    )

    constructor(evaluationTarget: EvaluationTarget, memberId: Long) : this(
        evaluationTarget.id,
        memberId,
        null,
        null,
        evaluationTarget.evaluationAnswers.countTotalScore(),
        evaluationTarget.evaluationStatus,
        evaluationTarget.administratorId ?: 0L,
        evaluationTarget.note,
        evaluationTarget.evaluationAnswers.answers.map(::EvaluationAnswerResponse),
    )
}

data class EvaluationAnswerResponse(
    val score: Int,
    val evaluationItemId: Long,
) {
    constructor(answer: EvaluationAnswer) : this(answer.score, answer.evaluationItemId)
}
