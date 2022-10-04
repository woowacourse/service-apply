package apply.application

import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus

data class LastJudgmentResponse(
    val status: JudgmentStatus,
    val pullRequestUrl: String,
    val commit: Commit,
    val result: JudgmentResult
)
