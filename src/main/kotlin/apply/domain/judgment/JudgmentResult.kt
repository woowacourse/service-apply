package apply.domain.judgment

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class JudgmentResult(
    @Column(nullable = false)
    val passCount: Int = 0,

    @Column(nullable = false)
    val totalCount: Int = 0,

    @Column(nullable = false)
    val message: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: JudgmentStatus = JudgmentStatus.STARTED
) {
    init {
        require(totalCount >= passCount)
    }
}
