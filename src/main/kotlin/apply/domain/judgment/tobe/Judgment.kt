package apply.domain.judgment.tobe

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ForeignKey
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
class Judgment(
    @Column(nullable = false)
    val assignmentId: Long,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: JudgmentType,
    records: List<JudgmentRecord> = emptyList(),
    id: Long = 0L
) : BaseEntity(id) {
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @JoinColumn(
        name = "judgment_id", nullable = false, updatable = false,
        foreignKey = ForeignKey(name = "fk_judgment_record_judgment_id_ref_judgment_id")
    )
    private val records: MutableList<JudgmentRecord> = records.toMutableList()

    val lastCommit: Commit
        get() = lastRecord.commit

    val lastStatus: JudgmentStatus
        get() = lastRecord.status

    private val lastRecord: JudgmentRecord
        get() = records.maxByOrNull { it.startedDateTime } ?: throw NoSuchElementException()

    fun start(commit: Commit) {
        check(canStart()) { "자동 채점을 시작할 수 없습니다." }
        findRecord(commit)?.restart() ?: records.add(JudgmentRecord(commit))
    }

    private fun canStart(): Boolean {
        if (records.isEmpty()) return true
        return lastRecord.startedDateTime.plusMinutes(DELAY_MINUTES) < LocalDateTime.now()
    }

    fun success(commit: Commit, result: JudgmentResult) {
        val record = getRecord(commit)
        record.applyResult(result)
    }

    fun fail(commit: Commit, message: String) {
        val record = getRecord(commit)
        record.applyResult(JudgmentResult(message = message))
    }

    private fun getRecord(commit: Commit): JudgmentRecord {
        return findRecord(commit) ?: throw NoSuchElementException("커밋이 존재하지 않습니다. commit: $commit")
    }

    fun completed(commit: Commit): Boolean {
        val record = findRecord(commit)
        return record != null && record.completed
    }

    private fun findRecord(commit: Commit): JudgmentRecord? {
        return records.find { it.commit == commit }
    }

    companion object {
        private const val DELAY_MINUTES: Long = 5
    }
}
