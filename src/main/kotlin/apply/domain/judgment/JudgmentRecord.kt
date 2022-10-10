package apply.domain.judgment

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
    result: JudgmentResult = JudgmentResult(),
    startedDateTime: LocalDateTime = LocalDateTime.now(),
    completedDateTime: LocalDateTime? = null
) : BaseEntity() {
    @Embedded
    var result: JudgmentResult = result
        private set

    @Column(nullable = false)
    var startedDateTime: LocalDateTime = startedDateTime
        private set

    var completedDateTime: LocalDateTime? = completedDateTime
        private set

    val completed: Boolean
        get() = status in listOf(JudgmentStatus.SUCCEEDED, JudgmentStatus.FAILED, JudgmentStatus.CANCELLED)

    val touchable: Boolean
        get() = status in listOf(JudgmentStatus.SUCCEEDED, JudgmentStatus.FAILED)

    val status: JudgmentStatus
        get() = result.status

    init {
        require(completed && completedDateTime != null || !completed && completedDateTime == null)
    }

    fun start() {
        result = JudgmentResult()
        startedDateTime = LocalDateTime.now()
        completedDateTime = null
    }

    fun touch() {
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
