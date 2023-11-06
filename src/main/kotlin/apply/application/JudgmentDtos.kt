package apply.application

import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentRecord
import apply.domain.judgment.JudgmentStatus
import apply.domain.judgment.JudgmentType
import apply.domain.judgmentitem.ProgrammingLanguage
import apply.domain.mission.SubmissionMethod
import java.time.LocalDateTime

data class LastJudgmentResponse(
    val url: String,
    val commitHash: String,
    val status: JudgmentStatus,
    val passCount: Int = 0,
    val totalCount: Int = 0,
    val message: String = "",
    val startedDateTime: LocalDateTime
) {
    val commitUrl: String
        get() = "$url/commits/$commitHash"

    constructor(url: String, record: JudgmentRecord) : this(
        url,
        record.commit.hash,
        record.status,
        record.result.passCount,
        record.result.totalCount,
        record.result.message,
        record.startedDateTime
    )
}

data class JudgmentRequest(
    val judgmentId: Long,
    val judgmentType: JudgmentType,
    val programmingLanguage: ProgrammingLanguage,
    val testName: String,
    val submissionMethod: SubmissionMethod,
    val url: String,
    val commit: Commit
)

data class SuccessJudgmentRequest(
    val commit: String,
    val passCount: Int,
    val totalCount: Int
)

data class FailJudgmentRequest(
    val commit: String,
    val message: String
)

data class CancelJudgmentRequest(
    val commit: String,
    val message: String
)

data class JudgmentData(
    val evaluationItemId: Long,
    val assignmentId: Long,
    val commitHash: String?,
    val status: JudgmentStatus?,
    val passCount: Int?,
    val totalCount: Int?,
    val message: String?,
    val startedDateTime: LocalDateTime?,
    val id: Long
) {
    constructor(
        id: Long? = null,
        evaluationItemId: Long,
        assignmentId: Long? = null,
        judgmentRecord: JudgmentRecord? = null
    ) : this(
        evaluationItemId,
        assignmentId ?: 0L,
        judgmentRecord?.commit?.hash,
        judgmentRecord?.status,
        judgmentRecord?.result?.passCount,
        judgmentRecord?.result?.totalCount,
        judgmentRecord?.result?.message,
        judgmentRecord?.startedDateTime,
        id ?: 0L
    )
}
