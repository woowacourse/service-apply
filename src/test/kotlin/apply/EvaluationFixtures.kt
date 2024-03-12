package apply

import apply.application.EvaluationItemData
import apply.application.EvaluationItemScoreData
import apply.domain.evaluation.Evaluation
import apply.domain.evaluationitem.EvaluationItem
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.ui.admin.evaluation.EvaluationItemForm
import apply.ui.admin.selections.EvaluationItemScoreForm

const val EVALUATION_ID = 1L
const val EVALUATION_TITLE1 = "프리코스 대상자 선발"
const val EVALUATION_TITLE2 = "1주차 - 숫자야구게임"
const val EVALUATION_TITLE3 = "2주차 - 자동차경주게임 "
const val EVALUATION_DESCRIPTION = "[리뷰 절차]\n" +
    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"

const val EVALUATION_ITEM_ID = 2L
const val EVALUATION_ITEM_TITLE = "평가 항목 제목"
const val EVALUATION_ITEM_DESCRIPTION = "평가 항목 설명"
const val EVALUATION_ITEM_MAXIMUM_SCORE = 3
const val EVALUATION_ITEM_POSITION = 1

const val EVALUATION_TARGET_NOTE = "평가 특이 사항"
const val EVALUATION_ANSWER_SCORE = 3

fun createEvaluation(
    title: String = EVALUATION_TITLE1,
    description: String = EVALUATION_DESCRIPTION,
    recruitmentId: Long = 1L,
    beforeEvaluationId: Long = 0L,
    id: Long = 0L
): Evaluation {
    return Evaluation(title, description, recruitmentId, beforeEvaluationId, id)
}

fun createEvaluationItem(
    title: String = EVALUATION_ITEM_TITLE,
    description: String = EVALUATION_ITEM_DESCRIPTION,
    evaluationId: Long = EVALUATION_ID,
    maximumScore: Int = EVALUATION_ITEM_MAXIMUM_SCORE,
    position: Int = EVALUATION_ITEM_POSITION,
    id: Long = 0L
): EvaluationItem {
    return EvaluationItem(title, description, maximumScore, position, evaluationId, id)
}

fun createEvaluationItemData(
    title: String = EVALUATION_ITEM_TITLE,
    description: String = EVALUATION_ITEM_DESCRIPTION,
    maximumScore: Int = EVALUATION_ITEM_MAXIMUM_SCORE,
    position: Int = EVALUATION_ITEM_POSITION,
    id: Long = 0L
): EvaluationItemData {
    return EvaluationItemData(EvaluationItem(title, description, maximumScore, position, id))
}

fun createEvaluationTarget(
    evaluationId: Long = EVALUATION_ID,
    memberId: Long = 1L,
    evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING,
    note: String = EVALUATION_TARGET_NOTE,
    evaluationAnswers: EvaluationAnswers = EvaluationAnswers(mutableListOf()),
    administratorId: Long? = null
): EvaluationTarget {
    return EvaluationTarget(
        evaluationId = evaluationId,
        administratorId = administratorId,
        memberId = memberId,
        evaluationStatus = evaluationStatus,
        evaluationAnswers = evaluationAnswers,
        note = note
    )
}

fun createEvaluationAnswer(
    score: Int = EVALUATION_ANSWER_SCORE,
    evaluationItemId: Long = EVALUATION_ITEM_ID
): EvaluationAnswer {
    return EvaluationAnswer(score, evaluationItemId)
}

fun createEvaluationItemForm(
    title: String = EVALUATION_ITEM_TITLE,
    maximumScore: Int = EVALUATION_ITEM_MAXIMUM_SCORE,
    position: Int = EVALUATION_ITEM_POSITION,
    description: String = EVALUATION_ITEM_DESCRIPTION
): EvaluationItemForm {
    return EvaluationItemForm(title, maximumScore, position, description)
}

fun createEvaluationAnswerForm(
    title: String = EVALUATION_ITEM_TITLE,
    description: String = EVALUATION_ITEM_DESCRIPTION,
    maximumScore: Int = EVALUATION_ITEM_MAXIMUM_SCORE,
    score: Int = EVALUATION_ANSWER_SCORE,
    evaluationItemId: Long = EVALUATION_ITEM_ID
): EvaluationItemScoreForm {
    return EvaluationItemScoreForm(title, description, maximumScore)
        .apply { fill(EvaluationItemScoreData(score, evaluationItemId)) }
}
