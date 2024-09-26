package apply

import apply.application.AssignmentData
import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.domain.assignment.Assignment

const val PULL_REQUEST_URL: String = "https://github.com/woowacourse/service-apply/pull/367"
private const val NOTE: String = "과제 소감입니다."

fun createAssignment(
    memberId: Long = 1L,
    missionId: Long = 1L,
    url: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L,
): Assignment {
    return Assignment(memberId, missionId, url, note, id)
}

fun createAssignmentRequest(
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
): AssignmentRequest {
    return AssignmentRequest(pullRequestUrl, note)
}

fun createAssignmentResponse(
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L,
): AssignmentResponse {
    return AssignmentResponse(id, pullRequestUrl, note)
}

fun createAssignmentData(
    pullRequestUrl: String = PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L,
): AssignmentData {
    return AssignmentData(pullRequestUrl, note, id)
}
