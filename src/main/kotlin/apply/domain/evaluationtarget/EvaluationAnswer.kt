package apply.domain.evaluationtarget

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class EvaluationAnswer(
    @Column(nullable = false)
    val score: Int = 0,

    @Column(nullable = false)
    val evaluationItemId: Long
)
