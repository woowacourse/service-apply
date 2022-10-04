package apply.domain.judgment

data class JudgmentStartedEvent(val judgmentId: Long, val commit: Commit)
