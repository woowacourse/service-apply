package apply

import apply.application.CreateAssignmentRequest
import apply.domain.assignment.Assignment

private const val GITHUB_USERNAME: String = "ecsimsw"
private const val PULL_REQUEST_URL: String = "https://github.com/woowacourse/service-apply/pull/367"
private const val NOTE = "과제 소감입니다."

fun createAssignment(
    userId: Long = 1L,
    missionId: Long = 1L,
    githubUsername: String = GITHUB_USERNAME,
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L
): Assignment {
    return Assignment(userId, missionId, githubUsername, pullRequestUrl, note, id)
}

fun createAssignmentRequest(
    githubUsername: String = GITHUB_USERNAME,
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
): CreateAssignmentRequest {
    return CreateAssignmentRequest(githubUsername, pullRequestUrl, note)
}
