package apply.domain.evaluationtarget

import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.FetchType

@Embeddable
class EvaluationAnswers(
    @ElementCollection(fetch = FetchType.EAGER)
    private val evaluationAnswers: MutableList<EvaluationAnswer> = mutableListOf()
) {
    fun add(evaluationAnswer: EvaluationAnswer) {
        evaluationAnswers.add(evaluationAnswer)
    }

    fun findScoreByEvaluationItemId(evaluationItemId: Long): Int? =
        evaluationAnswers.find { it.evaluationItemId == evaluationItemId }?.score
}
