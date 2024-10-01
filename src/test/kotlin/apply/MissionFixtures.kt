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
import apply.domain.mission.SubmissionMethod
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
    evaluationId: Long = 1L,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = MISSION_DESCRIPTION,
    submittable: Boolean = true,
    hidden: Boolean = false,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    id: Long = 0L,
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
    id: Long = 0L,
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
    hidden: Boolean = false,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    id: Long = 0L,
): MissionResponse {
    return MissionResponse(
        id,
        title,
        startDateTime,
        endDateTime,
        description,
        submittable,
        hidden,
        submissionMethod
    )
}

fun createMyMissionAndJudgementResponse(
    title: String = MISSION_TITLE,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    submittable: Boolean = true,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    status: MissionStatus = MissionStatus.SUBMITTING,
    submitted: Boolean = false,
    testable: Boolean = false,
    judgment: LastJudgmentResponse? = null,
    id: Long = 0L,
): MyMissionAndJudgementResponse {
    return MyMissionAndJudgementResponse(
        id,
        title,
        startDateTime,
        endDateTime,
        submittable,
        submissionMethod,
        status,
        submitted,
        testable,
        judgment
    )
}

fun createMyMissionResponse(
    title: String = MISSION_TITLE,
    startDateTime: LocalDateTime = START_DATE_TIME,
    endDateTime: LocalDateTime = END_DATE_TIME,
    description: String = FORMATTED_MISSION_DESCRIPTION,
    submittable: Boolean = true,
    submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    status: MissionStatus = MissionStatus.SUBMITTING,
    submitted: Boolean = true,
    id: Long = 0L,
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
        submitted
    )
}
