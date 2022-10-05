package apply.application

import apply.domain.judgment.JudgmentRecord
import apply.domain.judgment.JudgmentStatus
import java.time.LocalDateTime

data class LastJudgmentResponse(
    val pullRequestUrl: String,
    val commitHash: String,
    val status: JudgmentStatus,
    val passCount: Int = 0,
    val totalCount: Int = 0,
    val message: String = "",
    val startedDateTime: LocalDateTime
) {
    val commitUrl: String
        get() = "$pullRequestUrl/commits/$commitHash"

    constructor(pullRequestUrl: String, record: JudgmentRecord) : this(
        pullRequestUrl,
        record.commit.hash,
        record.status,
        record.result.passCount,
        record.result.totalCount,
        record.result.message,
        record.startedDateTime
    )
}
