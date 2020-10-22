package apply.application

import apply.domain.applicant.Applicant
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget

data class EvaluationTargetResponse(
    val id: Long,
    val name: String,
    val email: String,
    val totalScore: Int,
    val evaluationStatus: EvaluationStatus,
    val administratorId: Long,
    val note: String,
    val answers: List<EvaluationAnswerResponse>
) {
    constructor(
        id: Long,
        name: String,
        email: String,
        totalScore: Int,
        evaluationStatus: EvaluationStatus,
        administratorId: Long?,
        note: String,
        answers: EvaluationAnswers
    ) : this(
        id,
        name,
        email,
        totalScore,
        evaluationStatus,
        administratorId ?: 0L,
        note,
        answers.answers.map(::EvaluationAnswerResponse)
    )

    constructor(evaluationTarget: EvaluationTarget, applicant: Applicant) : this(
        evaluationTarget.id,
        applicant.name,
        applicant.email,
        evaluationTarget.evaluationAnswers.countTotalScore(),
        evaluationTarget.evaluationStatus,
        evaluationTarget.administratorId,
        evaluationTarget.note,
        evaluationTarget.evaluationAnswers
    )
}

data class EvaluationAnswerResponse(
    val score: Int,
    val evaluationItemId: Long
) {
    constructor(answer: EvaluationAnswer) : this(answer.score, answer.evaluationItemId)
}
