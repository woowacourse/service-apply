package apply.application

import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus

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
        EvaluationAnswerResponse.from(answers)
    )
}

data class EvaluationAnswerResponse(
    val score: Int,
    val evaluationItemId: Long
) {
    companion object {
        fun from(answers: EvaluationAnswers): List<EvaluationAnswerResponse> {
            return answers.answers.map(::EvaluationAnswerResponse)
        }
    }

    constructor(answer: EvaluationAnswer) : this(answer.score, answer.evaluationItemId)
}
