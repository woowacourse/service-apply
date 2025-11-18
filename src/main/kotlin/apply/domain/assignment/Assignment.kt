package apply.domain.assignment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Lob
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_assignment", columnNames = ["memberId", "missionId"])
    ],
    indexes = [Index(name = "idx_member_id", columnList = "memberId")]
)
@Entity
class Assignment(
    @Column(nullable = false)
    val memberId: Long,

    @Column(nullable = false)
    val missionId: Long,
    url: Url,

    @Column(nullable = false)
    @Lob
    var note: String,
    id: Long = 0L,
) : BaseEntity(id) {
    @Column(name = "url", nullable = false)
    private var _url: Url = url
    val url: String
        get() = this._url.value

    init {
        validate(note)
    }

    fun update(url: Url, note: String) {
        validate(note)
        this._url = url
        this.note = note
    }

    private fun validate(note: String) {
        require(note.length <= MAXIMUM_NOTE_LENGTH) { "소감의 길이는 ${MAXIMUM_NOTE_LENGTH}자를 초과할 수 없습니다." }
    }

    companion object {
        private const val MAXIMUM_NOTE_LENGTH: Int = 10_000
    }
}
