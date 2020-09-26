package apply.domain.evaluationItem

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class EvaluationItem(
    @Column(nullable = false)
    val evaluationId: Long,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val maximumScore: Int = 10,

    @Column(nullable = false)
    val position: Int = 0,

    @Column(nullable = false)
    val description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
