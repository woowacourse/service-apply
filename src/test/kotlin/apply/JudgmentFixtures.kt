package apply

import apply.application.FailJudgmentRequest
import apply.application.LastJudgmentResponse
import apply.application.SuccessJudgmentRequest
import apply.domain.judgment.Commit
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRecord
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus
import apply.domain.judgment.JudgmentType
import apply.domain.mission.JudgmentItem
import apply.domain.mission.ProgrammingLanguage
import java.time.LocalDateTime
import java.time.LocalDateTime.now

const val COMMIT_HASH: String = "642951e1324eaf66914bd53df339d94cad5667e3"

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
    result: JudgmentResult = createJudgmentResult(),
    startedDateTime: LocalDateTime = now().minusMinutes(10),
    completedDateTime: LocalDateTime? = now().minusMinutes(5)
): JudgmentRecord {
    return JudgmentRecord(commit, result, startedDateTime, completedDateTime)
}

fun createJudgmentResult(
    passCount: Int = 5,
    totalCount: Int = 10,
    message: String = "",
): JudgmentResult {
    return JudgmentResult(passCount, totalCount, message)
}

fun createCommit(
    hash: String = COMMIT_HASH
): Commit {
    return Commit(hash)
}

fun createLastJudgmentResponse(
    pullRequestUrl: String = PULL_REQUEST_URL,
    commitHash: String = COMMIT_HASH,
    status: JudgmentStatus = JudgmentStatus.STARTED,
    passCount: Int = 0,
    totalCount: Int = 0,
    message: String = "",
    startedDateTime: LocalDateTime = now()
): LastJudgmentResponse {
    return LastJudgmentResponse(pullRequestUrl, commitHash, status, passCount, totalCount, message, startedDateTime)
}

fun createJudgmentItem(
    missionId: Long = 1L,
    evaluationItemId: Long = 1L,
    testName: String = "test-name",
    programmingLanguage: ProgrammingLanguage = ProgrammingLanguage.KOTLIN,
    id: Long = 0L
): JudgmentItem {
    return JudgmentItem(missionId, evaluationItemId, testName, programmingLanguage, id)
}

fun createJudgmentSuccessRequest(
    judgmentId: Long = 1L,
    commit: String = COMMIT_HASH,
    passCount: Int = 5,
    totalCount: Int = 10
): SuccessJudgmentRequest {
    return SuccessJudgmentRequest(judgmentId, commit, passCount, totalCount)
}

fun createJudgmentFailRequest(
    judgmentId: Long = 1L,
    commit: String = COMMIT_HASH,
    message: String = "빌드 실패"
): FailJudgmentRequest {
    return FailJudgmentRequest(judgmentId, commit, message)
}
