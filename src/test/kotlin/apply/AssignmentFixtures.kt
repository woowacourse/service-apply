package apply

import apply.application.AssignmentData
import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.domain.assignment.Assignment

const val PULL_REQUEST_URL: String = "https://github.com/woowacourse/service-apply/pull/367"
private const val NOTE = "과제 소감입니다."

fun createAssignment(
    memberId: Long = 1L,
    missionId: Long = 1L,
    githubUsername: String = GITHUB_USERNAME,
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L
): Assignment {
    return Assignment(memberId, missionId, githubUsername, pullRequestUrl, note, id)
}

fun createAssignmentRequest(
    githubUsername: String = GITHUB_USERNAME,
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
): AssignmentRequest {
    return AssignmentRequest(githubUsername, pullRequestUrl, note)
}

fun createAssignmentResponse(
    githubUsername: String = GITHUB_USERNAME,
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L
): AssignmentResponse {
    return AssignmentResponse(id, githubUsername, pullRequestUrl, note)
}

fun createAssignmentData(
    githubUsername: String = GITHUB_USERNAME,
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L
): AssignmentData {
    return AssignmentData(githubUsername, pullRequestUrl, note, id)
}
