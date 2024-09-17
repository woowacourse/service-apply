package apply

import apply.application.EvaluationSelectData
import apply.application.JudgmentItemData
import apply.application.LastJudgmentResponse
import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MyMissionAndJudgementResponse
import apply.application.MyMissionResponse
import apply.domain.mission.Mission
import apply.domain.mission.MissionStatus
import support.flattenByMargin
import java.time.LocalDateTime

private const val MISSION_TITLE: String = "숫자 야구"
private const val MISSION_DESCRIPTION: String = "과제 설명입니다."
private val FORMATTED_MISSION_DESCRIPTION: String =
    """
        |<h1>미션 - 숫자 야구 게임</h1>
        |<h2>🔍 진행 방식</h2>
        |<ul>
        |<li>미션은 <strong>기능 요구 사항, 프로그래밍 요구 사항, 과제 진행 요구 사항</strong> 세 가지로 구성되어 있다.</li>
        |<li>세 개의 요구 사항을 만족하기 위해 노력한다. 특히 기능을 구현하기 전에 기능 목록을 만들고, 기능 단위로 커밋 하는 방식으로 진행한다.</li>
        |<li>기능 요구 사항에 기재되지 않은 내용은 스스로 판단하여 구현한다.</li>
        |</ul>
    """.flattenByMargin()
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
    judgmentItemData: JudgmentItemData = JudgmentItemData(),
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
        judgmentItemData,
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
    id: Long = 0L
): MissionResponse {
    return MissionResponse(
        id,
        title,
        description,
        submittable,
        startDateTime,
        endDateTime,
        missionStatus
    )
}

fun createMyMissionAndJudgementResponse(
    title: String = MISSION_TITLE,
    submittable: Boolean = true,
    submitted: Boolean = true,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    missionStatus: MissionStatus = MissionStatus.SUBMITTING,
    testable: Boolean = true,
    judgment: LastJudgmentResponse? = createLastJudgmentResponse(),
    id: Long = 0L
): MyMissionAndJudgementResponse {
    return MyMissionAndJudgementResponse(
        id,
        title,
        submittable,
        submitted,
        startDateTime,
        endDateTime,
        missionStatus,
        testable,
        judgment
    )
}

fun createMyMissionResponse(
    title: String = MISSION_TITLE,
    description: String = FORMATTED_MISSION_DESCRIPTION,
    submittable: Boolean = true,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    missionStatus: MissionStatus = MissionStatus.SUBMITTING,
    submitted: Boolean = true,
    id: Long = 0L,
): MyMissionResponse {
    return MyMissionResponse(
        id,
        title,
        description,
        submittable,
        startDateTime,
        endDateTime,
        missionStatus,
        submitted,
    )
}
