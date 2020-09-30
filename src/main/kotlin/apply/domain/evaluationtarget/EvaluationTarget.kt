package apply.domain.evaluationtarget

import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class EvaluationTarget(
    @Column(nullable = false)
    val evaluationId: Long = 0L,

    // TODO 추후 채점자 개념이 들어가면 수정 필요
    val administratorId: Long? = null,

    @Column(nullable = false)
    val applicantId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING,

    @Embedded
    var evaluationAnswers: EvaluationAnswers = EvaluationAnswers(),

    var note: String = "",

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    constructor(evaluationId: Long, applicantId: Long) : this(
        evaluationId = evaluationId,
        administratorId = 0L,
        applicantId = applicantId
    )

    val isPassed: Boolean
        get() = this.evaluationStatus == EvaluationStatus.PASS

    fun update(evaluationStatus: EvaluationStatus, evaluationAnswers: EvaluationAnswers, note: String) {
        this.evaluationStatus = evaluationStatus
        this.evaluationAnswers = evaluationAnswers
        this.note = note
    }
}
