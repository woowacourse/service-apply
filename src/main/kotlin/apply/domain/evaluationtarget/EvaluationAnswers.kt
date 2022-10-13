package apply.domain.evaluationtarget

import javax.persistence.CollectionTable
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.FetchType

private const val DEFAULT_SCORE: Int = 0

@Embeddable
class EvaluationAnswers(
    answers: List<EvaluationAnswer> = emptyList()
) {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "evaluation_target_answers")
    private val _answers: MutableList<EvaluationAnswer> = answers.toMutableList()
    val answers: List<EvaluationAnswer>
        get() = _answers

    fun add(evaluationItemId: Long, score: Int) {
        _answers.removeIf { it.evaluationItemId == evaluationItemId }
        _answers.add(EvaluationAnswer(score, evaluationItemId))
    }

    fun allZero(): Boolean = answers.all { it.score == 0 }

    fun countTotalScore(): Int {
        return _answers.sumOf { it.score }
    }

    fun findScoreByEvaluationItemId(evaluationItemId: Long): Int =
        _answers.find { it.evaluationItemId == evaluationItemId }?.score ?: DEFAULT_SCORE
}
