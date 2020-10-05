package apply.domain.evaluationtarget

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class EvaluationAnswer(
    @Column(nullable = false)
    var score: Int = 0,

    @Column(nullable = false)
    val evaluationItemId: Long
) {
    fun update(score: Int) {
        require(score >= 0)
        this.score = score
    }
}
