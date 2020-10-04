package apply

import apply.domain.evaluation.Evaluation
import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget

const val EVALUATION_ID = 1L
const val EVALUATION_TITLE1 = "프리코스 대상자 선발"
const val EVALUATION_TITLE2 = "1주차 - 숫자야구게임"
const val EVALUATION_TITLE3 = "2주차 - 자동차경주게임 "
const val EVALUATION_DESCRIPTION = "[리뷰 절차]\n" +
    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"

const val EVALUATION_ITEM_ID = 2L
const val EVALUATION_ITEM_TITLE = "평가 항목 제목"
const val EVALUATION_ITEM_DESCRIPTION = "평가 항목 설명"

const val NOTE = "평가 특이 사항"
const val SCORE = 3

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
    maximumScore: Int = 3,
    position: Int = 0,
    id: Long = 0L
): EvaluationItem {
    return EvaluationItem(title, description, maximumScore, position, evaluationId, id)
}

fun createEvaluationTarget(
    evaluationId: Long = EVALUATION_ID,
    applicantId: Long = 1L,
    evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING,
    note: String = NOTE,
    evaluationAnswers: EvaluationAnswers = EvaluationAnswers(mutableListOf()),
    administratorId: Long? = null
): EvaluationTarget {
    return EvaluationTarget(
        evaluationId = evaluationId,
        administratorId = administratorId,
        applicantId = applicantId,
        evaluationStatus = evaluationStatus,
        evaluationAnswers = evaluationAnswers,
        note = note
    )
}

fun createEvaluationAnswer(
    score: Int = SCORE,
    evaluationItemId: Long = EVALUATION_ITEM_ID
): EvaluationAnswer {
    return EvaluationAnswer(score, evaluationItemId)
}
