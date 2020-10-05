package apply.domain.evaluationtarget

import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.FetchType

@Embeddable
class EvaluationAnswers(
    @ElementCollection(fetch = FetchType.EAGER)
    val answers: MutableList<EvaluationAnswer> = mutableListOf()
) {
    fun add(evaluationAnswer: EvaluationAnswer) {
        answers.add(evaluationAnswer)
    }
}
