package apply.domain.judgment.tobe

data class LastJudgmentResponse(
    val status: JudgmentStatus,
    val pullRequestUrl: String,
    val commit: Commit,
    val result: JudgmentResult
)
