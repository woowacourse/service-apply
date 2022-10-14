package apply.domain.judgment

import support.domain.BaseRootEntity
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
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
) : BaseRootEntity<Judgment>(id) {
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE], fetch = FetchType.EAGER)
    @JoinColumn(
        name = "judgment_id", nullable = false, updatable = false,
        foreignKey = ForeignKey(name = "fk_judgment_record_judgment_id_ref_judgment_id")
    )
    private val records: MutableList<JudgmentRecord> = records.toMutableList()

    val lastCommit: Commit
        get() = lastRecord.commit

    val lastStatus: JudgmentStatus
        get() = lastRecord.status

    val lastRecord: JudgmentRecord
        get() = records.maxByOrNull { it.startedDateTime } ?: throw NoSuchElementException()

    fun start(commit: Commit) {
        check(canStart()) { "자동 채점을 시작할 수 없습니다." }
        val record = findRecord(commit) ?: createRecord(commit)
        if (record.touchable) {
            record.touch()
            val event = JudgmentTouchedEvent(id, assignmentId, type, record.result.passCount, record.result.totalCount)
            registerEvent(event)
        } else {
            record.start()
            registerEvent(JudgmentStartedEvent(id, assignmentId, type, commit))
        }
    }

    private fun canStart(): Boolean {
        if (records.isEmpty()) return true
        return lastRecord.completed || lastRecord.startedDateTime.plusMinutes(DELAY_MINUTES) < LocalDateTime.now()
    }

    private fun createRecord(commit: Commit): JudgmentRecord {
        return JudgmentRecord(commit).also { records.add(it) }
    }

    fun success(commit: Commit, passCount: Int, totalCount: Int) {
        val record = getRecord(commit)
        record.applyResult(JudgmentResult(passCount, totalCount, status = JudgmentStatus.SUCCEEDED))
        registerEvent(JudgmentSucceededEvent(id, assignmentId, type, passCount, totalCount))
    }

    fun fail(commit: Commit, message: String) {
        val record = getRecord(commit)
        record.applyResult(JudgmentResult(message = message, status = JudgmentStatus.FAILED))
        registerEvent(JudgmentFailedEvent(id, assignmentId, type))
    }

    fun cancel(commit: Commit, message: String) {
        val record = getRecord(commit)
        record.applyResult(JudgmentResult(message = message, status = JudgmentStatus.CANCELLED))
        registerEvent(JudgmentCancelledEvent(id, assignmentId, type))
    }

    private fun getRecord(commit: Commit): JudgmentRecord = findRecord(commit)
        ?: throw NoSuchElementException("커밋이 존재하지 않습니다. commit: $commit")

    private fun findRecord(commit: Commit): JudgmentRecord? = records.find { it.commit == commit }

    companion object {
        private const val DELAY_MINUTES: Long = 5
    }
}
