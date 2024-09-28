package apply

import apply.application.AssignmentData
import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.domain.assignment.Assignment

const val PUBLIC_PULL_REQUEST_URL: String = "https://github.com/woowacourse/service-apply/pull/367"
private const val NOTE: String = "과제 소감입니다."

fun createAssignment(
    memberId: Long = 1L,
    missionId: Long = 1L,
    url: String = PUBLIC_PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L,
): Assignment {
    return Assignment(memberId, missionId, url, note, id)
}

fun createAssignmentRequest(
    url: String = PUBLIC_PULL_REQUEST_URL,
    note: String = NOTE,
): AssignmentRequest {
    return AssignmentRequest(url, note)
}

fun createAssignmentResponse(
    url: String = PUBLIC_PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L,
): AssignmentResponse {
    return AssignmentResponse(id, url, note)
}

fun createAssignmentData(
    url: String = PUBLIC_PULL_REQUEST_URL,
    note: String = NOTE,
    id: Long = 0L,
): AssignmentData {
    return AssignmentData(url, note, id)
}
