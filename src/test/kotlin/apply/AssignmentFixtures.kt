package apply

import apply.application.CreateAssignmentRequest
import apply.domain.assignment.Assignment

private const val GIT_ACCOUNT: String = "ecsimsw"
private const val URL: String = "https://github.com/woowacourse/service-apply/pull/367"
private const val IMPRESSION = "과제 소감입니다."

fun createAssignment(
    applicantId: Long = 1L,
    missionId: Long = 1L,
    gitAccount: String = GIT_ACCOUNT,
    url: String = URL,
    impression: String = IMPRESSION,
    id: Long = 0L
): Assignment {
    return Assignment(applicantId, missionId, gitAccount, url, impression, id)
}

fun createAssignmentRequest(
    gitAccount: String = GIT_ACCOUNT,
    url: String = URL,
    impression: String = IMPRESSION,
): CreateAssignmentRequest {
    return CreateAssignmentRequest(gitAccount, url, impression)
}
