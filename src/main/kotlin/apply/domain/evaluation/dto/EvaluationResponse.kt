package apply.domain.evaluation.dto

data class EvaluationResponse(
    val id: Long,
    val title: String,
    val description: String,
    val recruitment: String,
    val recruitmentId: Long,
    val beforeEvaluation: String = "",
    val beforeEvaluationId: Long = 0L
)
