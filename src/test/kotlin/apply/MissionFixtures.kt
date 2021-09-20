package apply

import apply.application.EvaluationSelectData
import apply.application.MissionData
import apply.domain.mission.Mission
import java.time.LocalDateTime

private const val MISSION_TITLE: String = "숫자야구게임"
private const val MISSION_DESCRIPTION: String = "과제 설명입니다."
private val START_DATE_TIME: LocalDateTime = LocalDateTime.now()
private val END_DATE_TIME: LocalDateTime = LocalDateTime.now().plusDays(7)

fun createMission(
    title: String = MISSION_TITLE,
    description: String = MISSION_DESCRIPTION,
    evaluationId: Long = 1L,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    submittable: Boolean = true,
    id: Long = 0L
): Mission {
    return Mission(title, description, evaluationId, startDateTime, endDateTime, submittable, id)
}

fun createMissionData(
    title: String = MISSION_TITLE,
    evaluation: EvaluationSelectData = EvaluationSelectData(),
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    id: Long = 0L
): MissionData {
    return MissionData(title, evaluation, startDateTime, endDateTime, description, submittable, id)
}
