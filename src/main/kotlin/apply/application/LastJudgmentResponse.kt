package apply.application

import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentRecord
import apply.domain.judgment.JudgmentResult
import java.time.LocalDateTime

data class LastJudgmentResponse(
    val pullRequestUrl: String,
    val commit: Commit,
    val result: JudgmentResult,
    val startedDateTime: LocalDateTime
) {
    val commitUrl: String
        get() = "$pullRequestUrl/commits/${commit.hash}"

    constructor(pullRequestUrl: String, record: JudgmentRecord) : this(
        pullRequestUrl,
        record.commit,
        record.result,
        record.startedDateTime
    )
}
