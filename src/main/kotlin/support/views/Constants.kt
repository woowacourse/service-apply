package support.views

import apply.domain.evaluationtarget.EvaluationStatus

const val NO_NAME: String = "(이름 없음)"
const val WITHDRAWN_NAME: String = "(탈퇴 회원)"

fun Boolean?.toText(): String? {
    return when (this) {
        true -> "O"
        false -> "X"
        else -> null
    }
}

fun EvaluationStatus.toText(): String {
    return when (this) {
        EvaluationStatus.WAITING -> "평가 전"
        EvaluationStatus.PASS -> "합격"
        EvaluationStatus.FAIL -> "탈락"
        EvaluationStatus.PENDING -> "보류"
    }
}
