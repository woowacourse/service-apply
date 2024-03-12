package apply.domain.evaluationtarget

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class EvaluationTarget(
    @Column(nullable = false)
    val evaluationId: Long = 0L,

    // TODO 추후 채점자 개념이 들어가면 수정 필요
    val administratorId: Long? = null,

    @Column(nullable = false)
    val memberId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var evaluationStatus: EvaluationStatus = EvaluationStatus.WAITING,

    @Embedded
    var evaluationAnswers: EvaluationAnswers = EvaluationAnswers(),
    var note: String = "",
    id: Long = 0L
) : BaseEntity(id) {
    constructor(evaluationId: Long, memberId: Long) : this(
        evaluationId = evaluationId,
        administratorId = 0L,
        memberId = memberId
    )

    val isPassed: Boolean
        get() = this.evaluationStatus == EvaluationStatus.PASS

    fun passIfBeforeEvaluation() {
        if (evaluationStatus == EvaluationStatus.WAITING) {
            evaluationStatus = EvaluationStatus.PASS
        }
    }

    fun evaluated(): Boolean {
        return !evaluationAnswers.allZero()
    }

    fun update(evaluationStatus: EvaluationStatus, evaluationAnswers: EvaluationAnswers, note: String) {
        this.evaluationStatus = evaluationStatus
        this.evaluationAnswers = evaluationAnswers
        this.note = note
    }

    fun updateScore(evaluationItemId: Long, score: Int) {
        evaluationAnswers.add(evaluationItemId, score)
    }
}
