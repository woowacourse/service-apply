package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.mission.JudgmentItem
import apply.domain.mission.Mission
import apply.domain.mission.MissionStatus
import apply.domain.mission.ProgrammingLanguage
import apply.domain.mission.ProgrammingLanguage.NONE
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

    var testName: String? = "",
    var programmingLanguage: ProgrammingLanguage? = NONE,
    var evaluationItem: EvaluationItemData? = EvaluationItemData(),
    var judgmentItemId: Long = 0L,

    @field:NotNull
    var submittable: Boolean = false,

    @field:NotNull
    var hidden: Boolean = true,
    var id: Long = 0L
) {
    constructor(mission: Mission, evaluation: Evaluation, judgmentItemData: JudgmentItemData) : this(
        mission.title,
        EvaluationSelectData(evaluation),
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.description,

        judgmentItemData.testName ?: "",
        judgmentItemData.programmingLanguage ?: NONE,
        judgmentItemData.evaluationItemData ?: EvaluationItemData(
            title = "평가 항목 없음",
            maximumScore = 0,
            position = 0,
            description = "평가 항목 없음"
        ),
        judgmentItemData.id,

        mission.submittable,
        mission.hidden,
        mission.id
    )
}

data class MissionAndEvaluationResponse(
    val id: Long,
    val title: String,
    val description: String,
    val evaluationTitle: String,
    val evaluationId: Long,
    val submittable: Boolean,
    val status: MissionStatus,
    val hidden: Boolean,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
) {
    constructor(mission: Mission, evaluation: Evaluation) : this(
        mission.id,
        mission.title,
        mission.description,
        evaluation.title,
        evaluation.id,
        mission.submittable,
        mission.status,
        mission.hidden,
        mission.period.startDateTime,
        mission.period.endDateTime
    )
}

data class MissionResponse(
    val id: Long,
    val title: String,
    val description: String,
    val submittable: Boolean,
    val testName: String?,
    val programmingLanguage: ProgrammingLanguage?,
    val evaluationItemId: Long?,
    val judgmentItemId: Long?,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: MissionStatus
) {
    constructor(mission: Mission, judgmentItem: JudgmentItem?) : this(
        mission.id,
        mission.title,
        mission.description,
        mission.submittable,
        judgmentItem?.testName,
        judgmentItem?.programmingLanguage,
        judgmentItem?.evaluationItemId,
        judgmentItem?.id,
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.status
    )
}

data class MyMissionResponse(
    val id: Long,
    val title: String,
    val description: String,
    val submittable: Boolean,
    val submitted: Boolean,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: MissionStatus
) {
    constructor(mission: Mission, submitted: Boolean) : this(
        mission.id,
        mission.title,
        mission.description,
        mission.submittable,
        submitted,
        mission.period.startDateTime,
        mission.period.endDateTime,
        mission.status
    )
}

data class JudgmentItemData(
    val id: Long,
    val testName: String?,
    val evaluationItemData: EvaluationItemData?,
    val programmingLanguage: ProgrammingLanguage?
) {
    constructor(judgmentItem: JudgmentItem?, evaluationItemData: EvaluationItemData?) : this(
        judgmentItem?.id ?: 0L,
        judgmentItem?.testName,
        evaluationItemData,
        judgmentItem?.programmingLanguage
    )
}
