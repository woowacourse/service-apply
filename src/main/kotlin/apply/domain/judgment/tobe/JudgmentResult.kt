package apply.domain.judgment.tobe

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class JudgmentResult(
    @Column(nullable = false)
    val passCount: Int = 0,

    @Column(nullable = false)
    val totalCount: Int = 0,

    @Column(nullable = false)
    val message: String = ""
) {
    init {
        require(totalCount >= passCount)
    }

    val status: JudgmentStatus
        get() = when {
            message.isEmpty() && totalCount == 0 && passCount == 0 -> JudgmentStatus.STARTED
            totalCount != 0 -> JudgmentStatus.SUCCEEDED
            else -> JudgmentStatus.FAILED
        }
}
