package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluationitem.EvaluationItem
import apply.domain.judgmentitem.JudgmentItem
import apply.domain.judgmentitem.ProgrammingLanguage
import apply.domain.mission.Mission
import apply.domain.mission.MissionStatus
import apply.domain.mission.SubmissionMethod
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class MissionData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var title: String = "",

    @field:NotNull
    var evaluation: EvaluationSelectData = EvaluationSelectData(),

    @field:NotNull
    var startDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotNull
    var endDateTime: LocalDateTime = LocalDateTime.MIN,

    @field:NotBlank
    var description: String = "",

    @field:NotNull
    var submittable: Boolean = false,

    @field:NotNull
    var hidden: Boolean = true,

    @field:NotNull
    var submissionMethod: SubmissionMethod = SubmissionMethod.PUBLIC_PULL_REQUEST,
    var judgmentItemData: JudgmentItemData = JudgmentItemData(),
    var id: Long = 0L,
) {
    constructor(mission: Mission, evaluation: Evaluation, judgmentItemData: JudgmentItemData) : this(
        mission.title,
        EvaluationSelectData(evaluation),
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.description,
        mission.submittable,
        mission.hidden,
        mission.submissionMethod,
        judgmentItemData,
        mission.id
    )
}

data class MissionAndEvaluationResponse(
    val id: Long,
    val title: String,
    val evaluationId: Long,
    val evaluationTitle: String,
    val submittable: Boolean,
    val status: MissionStatus,
    val hidden: Boolean,
    val submissionMethod: SubmissionMethod,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
) {
    constructor(mission: Mission, evaluation: Evaluation) : this(
        mission.id,
        mission.title,
        evaluation.id,
        evaluation.title,
        mission.submittable,
        mission.status,
        mission.hidden,
        mission.submissionMethod,
        mission.period.startDateTime,
        mission.period.endDateTime
    )
}

data class MissionResponse(
    val id: Long,
    val title: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val description: String,
    val submittable: Boolean,
    val hidden: Boolean,
    val submissionMethod: SubmissionMethod,
) {
    constructor(mission: Mission) : this(
        mission.id,
        mission.title,
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.description,
        mission.submittable,
        mission.hidden,
        mission.submissionMethod
    )
}

data class MyMissionAndJudgementResponse(
    val id: Long,
    val title: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val submittable: Boolean,
    val submissionMethod: SubmissionMethod,
    val status: MissionStatus,
    val submitted: Boolean,
    val testable: Boolean,
    val judgment: LastJudgmentResponse?,
) {
    constructor(
        mission: Mission,
        submitted: Boolean,
        testable: Boolean,
        judgment: LastJudgmentResponse? = null,
    ) : this(
        mission.id,
        mission.title,
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.submittable,
        mission.submissionMethod,
        mission.status,
        submitted,
        testable,
        judgment
    )
}

data class MyMissionResponse(
    val id: Long,
    val title: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val description: String,
    val submittable: Boolean,
    val submissionMethod: SubmissionMethod,
    val status: MissionStatus,
    val submitted: Boolean,
) {
    constructor(mission: Mission, description: String, submitted: Boolean) : this(
        mission.id,
        mission.title,
        mission.period.startDateTime,
        mission.period.endDateTime,
        description,
        mission.submittable,
        mission.submissionMethod,
        mission.status,
        submitted
    )
}

data class JudgmentItemData(
    var id: Long = 0L,
    var testName: String = "",
    var evaluationItemSelectData: EvaluationItemSelectData = EvaluationItemSelectData(),
    var programmingLanguage: ProgrammingLanguage = ProgrammingLanguage.NONE,
) {
    constructor(judgmentItem: JudgmentItem, evaluationItemSelectData: EvaluationItemSelectData) : this(
        judgmentItem.id,
        judgmentItem.testName,
        evaluationItemSelectData,
        judgmentItem.programmingLanguage
    )
}

data class EvaluationItemSelectData(
    var title: String = "",
    var id: Long = 0L,
) {
    constructor(evaluationItem: EvaluationItem) : this(evaluationItem.title, evaluationItem.id)
}
