package apply.domain.assignment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_assignment", columnNames = ["userId", "missionId"])
    ],
    indexes = [Index(name = "idx_user_id", columnList = "userId")]
)
@Entity
class Assignment(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false, length = 39)
    var githubUsername: String,

    @Column(nullable = false)
    var url: String,

    @Column(nullable = false, length = 5000)
    var note: String,
    id: Long = 0L
) : BaseEntity(id) {
    fun update(githubUsername: String, url: String, note: String) {
        this.githubUsername = githubUsername
        this.url = url
        this.note = note
    }
}
