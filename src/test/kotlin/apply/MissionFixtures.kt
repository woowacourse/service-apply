package apply

import apply.application.EvaluationSelectData
import apply.application.JudgmentItemData
import apply.application.LastJudgmentResponse
import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MyMissionResponse
import apply.domain.mission.Mission
import apply.domain.mission.MissionStatus
import apply.domain.mission.SubmissionMethod
import java.time.LocalDateTime

private const val MISSION_TITLE: String = "숫자야구게임"
private const val MISSION_DESCRIPTION: String = "과제 설명입니다."
private val START_DATE_TIME: LocalDateTime = LocalDateTime.now()
private val END_DATE_TIME: LocalDateTime = LocalDateTime.now().plusDays(7L)

fun createMission(
    title: String = MISSION_TITLE,
    evaluationId: Long = 1L,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    hidden: Boolean = false,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    id: Long = 0L
): Mission {
    return Mission(
        title,
        evaluationId,
        startDateTime,
        endDateTime,
        description,
        submittable,
        hidden,
        submissionMethod,
        id
    )
}

fun createMissionData(
    title: String = MISSION_TITLE,
    evaluation: EvaluationSelectData = EvaluationSelectData(),
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    hidden: Boolean = true,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    judgmentItemData: JudgmentItemData = JudgmentItemData(),
    id: Long = 0L
): MissionData {
    return MissionData(
        title,
        evaluation,
        startDateTime,
        endDateTime,
        description,
        submittable,
        hidden,
        submissionMethod,
        judgmentItemData,
        id
    )
}

fun createMissionResponse(
    title: String = MISSION_TITLE,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    missionStatus: MissionStatus = MissionStatus.SUBMITTING,
    id: Long = 0L
): MissionResponse {
    return MissionResponse(
        id,
        title,
        startDateTime,
        endDateTime,
        description,
        submittable,
        submissionMethod,
        missionStatus
    )
}

fun createMyMissionResponse(
    title: String = MISSION_TITLE,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    status: MissionStatus = MissionStatus.SUBMITTING,
    submitted: Boolean = true,
    runnable: Boolean = true,
    judgment: LastJudgmentResponse? = createLastJudgmentResponse(),
    id: Long = 0L
): MyMissionResponse {
    return MyMissionResponse(
        id,
        title,
        startDateTime,
        endDateTime,
        description,
        submittable,
        submissionMethod,
        status,
        submitted,
        runnable,
        judgment
    )
}
