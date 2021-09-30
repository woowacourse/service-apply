package apply

import apply.application.CreateAssignmentRequest
import apply.domain.assignment.Assignment

private const val GITHUB_ID: String = "ecsimsw"
private const val URL: String = "https://github.com/woowacourse/service-apply/pull/367"
private const val IMPRESSION = "과제 소감입니다."

fun createAssignment(
    applicantId: Long = 1L,
    missionId: Long = 1L,
    githubId: String = GITHUB_ID,
    url: String = URL,
    impression: String = IMPRESSION,
    id: Long = 0L
): Assignment {
    return Assignment(applicantId, missionId, githubId, url, impression, id)
}

fun createAssignmentRequest(
    githubId: String = GITHUB_ID,
    url: String = URL,
    impression: String = IMPRESSION,
): CreateAssignmentRequest {
    return CreateAssignmentRequest(githubId, url, impression)
}
