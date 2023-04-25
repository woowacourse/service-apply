package apply.domain.evaluationtarget

enum class EvaluationStatus(val title: String) {
    WAITING("평가 전"),
    PENDING("보류"),
    PASS("합격"),
    FAIL("탈락")
}
