package apply.domain.judgment.tobe

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_judgment_record", columnNames = ["judgment_id", "commit_hash"])
    ]
)
@Entity
class JudgmentRecord(
    @Embedded
    val commit: Commit,

    @Embedded
    var result: JudgmentResult = JudgmentResult(),

    @Column(nullable = false)
    var startedDateTime: LocalDateTime = LocalDateTime.now(),
    var completedDateTime: LocalDateTime? = null
) : BaseEntity() {
    init {
        if (result.status == JudgmentStatus.STARTED) {
            require(completedDateTime == null)
        } else {
            requireNotNull(completedDateTime)
        }
    }

    val completed: Boolean
        get() = status in listOf(JudgmentStatus.SUCCEEDED, JudgmentStatus.FAILED)

    val status: JudgmentStatus
        get() = result.status

    fun start() {
        result = JudgmentResult()
        startedDateTime = LocalDateTime.now()
        completedDateTime = null
    }

    fun renew() {
        val now = LocalDateTime.now()
        startedDateTime = now
        completedDateTime = now
    }

    fun applyResult(result: JudgmentResult) {
        require(result.status != JudgmentStatus.STARTED)
        this.result = result
        completedDateTime = LocalDateTime.now()
    }
}
