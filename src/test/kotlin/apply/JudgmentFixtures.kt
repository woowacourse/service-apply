package apply

import apply.application.LastJudgmentResponse
import apply.domain.judgment.Commit
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRecord
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus
import apply.domain.judgment.JudgmentType
import java.time.LocalDateTime

fun createJudgment(
    assignmentId: Long = 1L,
    type: JudgmentType = JudgmentType.EXAMPLE,
    records: List<JudgmentRecord> = emptyList(),
    id: Long = 0L
): Judgment {
    return Judgment(assignmentId, type, records, id)
}

fun createJudgmentRecord(
    commit: Commit = createCommit(),
    result: JudgmentResult = JudgmentResult(),
    startedDateTime: LocalDateTime = LocalDateTime.now(),
    completedDateTime: LocalDateTime? = null
): JudgmentRecord {
    return JudgmentRecord(commit, result, startedDateTime, completedDateTime)
}

fun createCommit(
    hash: String = "589a9e2e2819399962312e412298b1b3923adaa9"
): Commit {
    return Commit(hash)
}

fun createLastJudgmentResponse(
    status: JudgmentStatus = JudgmentStatus.STARTED,
    pullRequestUrl: String = PULL_REQUEST_URL,
    commit: Commit = createCommit(),
    result: JudgmentResult = JudgmentResult()
): LastJudgmentResponse {
    return LastJudgmentResponse(status, pullRequestUrl, commit, result)
}
