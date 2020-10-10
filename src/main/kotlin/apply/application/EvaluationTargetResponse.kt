package apply.application

import apply.domain.evaluationtarget.EvaluationStatus

data class EvaluationTargetResponse(
    val id: Long,
    val name: String,
    val email: String,
    val totalScore: Int,
    val evaluationStatus: EvaluationStatus,
    val administratorId: Long
) {
    constructor(
        id: Long,
        name: String,
        email: String,
        totalScore: Int,
        evaluationStatus: EvaluationStatus,
        administratorId: Long?
    ) : this(
        id,
        name,
        email,
        totalScore,
        evaluationStatus,
        administratorId ?: 0L
    )
}
