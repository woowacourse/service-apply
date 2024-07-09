package apply.application

import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus

data class EvaluationTargetResponse(
    val id: Long,
    val name: String,
    val email: String,
    val memberId: Long,
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
        memberId: Long,
        totalScore: Int,
        evaluationStatus: EvaluationStatus,
        administratorId: Long?,
        note: String,
        answers: EvaluationAnswers
    ) : this(
        id,
        name,
        email,
        memberId,
        totalScore,
        evaluationStatus,
        administratorId ?: 0L,
        note,
        answers.answers.map(::EvaluationAnswerResponse)
    )
}

data class EvaluationAnswerResponse(
    val score: Int,
    val evaluationItemId: Long
) {
    constructor(answer: EvaluationAnswer) : this(answer.score, answer.evaluationItemId)
}
