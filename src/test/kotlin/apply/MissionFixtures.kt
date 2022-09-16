package apply

import apply.application.EvaluationItemData
import apply.application.EvaluationSelectData
import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MyMissionResponse
import apply.domain.mission.JudgeItem
import apply.domain.mission.Mission
import apply.domain.mission.MissionStatus
import apply.domain.mission.ProgrammingLanguage
import apply.domain.mission.ProgrammingLanguage.NONE
import java.time.LocalDateTime

private const val MISSION_TITLE: String = "숫자야구게임"
private const val MISSION_DESCRIPTION: String = "과제 설명입니다."
private val START_DATE_TIME: LocalDateTime = LocalDateTime.now()
private val END_DATE_TIME: LocalDateTime = LocalDateTime.now().plusDays(7L)

fun createMission(
    title: String = MISSION_TITLE,
    description: String = MISSION_DESCRIPTION,
    evaluationId: Long = 1L,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    submittable: Boolean = true,
    hidden: Boolean = false,
    id: Long = 0L
): Mission {
    return Mission(title, description, evaluationId, startDateTime, endDateTime, submittable, hidden, id)
}

fun createMissionData(
    title: String = MISSION_TITLE,
    evaluation: EvaluationSelectData = EvaluationSelectData(),
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    testName: String? = "",
    programmingLanguage: ProgrammingLanguage? = NONE,
    evaluationItemData: EvaluationItemData = EvaluationItemData(),
    judgeItemId: Long = 0L,
    submittable: Boolean = true,
    hidden: Boolean = true,
    id: Long = 0L
): MissionData {
    return MissionData(
        title,
        evaluation,
        startDateTime,
        endDateTime,
        description,
        testName,
        programmingLanguage,
        evaluationItemData,
        judgeItemId,
        submittable,
        hidden,
        id
    )
}

fun createMissionResponse(
    title: String = MISSION_TITLE,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    missionStatus: MissionStatus = MissionStatus.SUBMITTING,
    testName: String? = "",
    programmingLanguage: ProgrammingLanguage? = NONE,
    evaluationItemData: EvaluationItemData = EvaluationItemData(),
    judgeItemId: Long = 0L,
    id: Long = 0L
): MissionResponse {
    return MissionResponse(
        id,
        title,
        description,
        submittable,
        testName,
        programmingLanguage,
        evaluationItemData.id,
        judgeItemId,
        startDateTime,
        endDateTime,
        missionStatus
    )
}

fun createMyMissionResponse(
    title: String = MISSION_TITLE,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    submitted: Boolean = true,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    missionStatus: MissionStatus = MissionStatus.SUBMITTING,
    id: Long = 0L
): MyMissionResponse {
    return MyMissionResponse(id, title, description, submittable, submitted, startDateTime, endDateTime, missionStatus)
}

fun createJudgeItem(
    missionId: Long = 0L,
    evaluationItemId: Long? = 0L,
    testName: String? = "",
    programmingLanguage: ProgrammingLanguage? = NONE,
    id: Long = 0L
): JudgeItem {
    return JudgeItem(missionId, evaluationItemId, testName, programmingLanguage, id)
}
