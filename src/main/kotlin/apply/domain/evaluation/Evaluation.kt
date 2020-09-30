package apply.domain.evaluation

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Evaluation(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val recruitmentId: Long,

    var beforeEvaluationId: Long = 0L,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    fun hasBeforeEvaluation(): Boolean = beforeEvaluationId != 0L

    fun hasSameBeforeEvaluationWith(beforeEvaluationId: Long): Boolean = this.beforeEvaluationId == beforeEvaluationId

    fun resetBeforeEvaluation() {
        beforeEvaluationId = 0L
    }
}
