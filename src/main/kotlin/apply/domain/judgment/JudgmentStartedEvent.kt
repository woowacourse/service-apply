package apply.domain.judgment

data class JudgmentStartedEvent(
    val judgmentId: Long,
    val assignmentId: Long,
    val type: JudgmentType,
    val commit: Commit
)
