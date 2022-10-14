package apply.domain.judgment

data class JudgmentStartedEvent(
    val judgmentId: Long,
    val assignmentId: Long,
    val type: JudgmentType,
    val commit: Commit
)

data class JudgmentTouchedEvent(
    val judgmentId: Long,
    val assignmentId: Long,
    val type: JudgmentType,
    val passCount: Int,
    val totalCount: Int
)

data class JudgmentSucceededEvent(
    val judgmentId: Long,
    val assignmentId: Long,
    val type: JudgmentType,
    val passCount: Int,
    val totalCount: Int
)

data class JudgmentFailedEvent(
    val judgmentId: Long,
    val assignmentId: Long,
    val type: JudgmentType
)

data class JudgmentCancelledEvent(
    val judgmentId: Long,
    val assignmentId: Long,
    val type: JudgmentType
)
