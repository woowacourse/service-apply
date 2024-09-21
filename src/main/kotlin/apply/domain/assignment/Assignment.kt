package apply.domain.assignment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
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

    @Column(nullable = false)
    var url: Url,

    @Column(nullable = false, length = 5000)
    var note: String,
    id: Long = 0L,
) : BaseEntity(id) {
    val pullRequestUrl: String
        get() = url.value

    constructor(memberId: Long, missionId: Long, url: String, note: String) : this(memberId, missionId, Url(url), note)

    fun update(url: String, note: String) {
        this.url = Url(url)
        this.note = note
    }
}
