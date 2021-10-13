package apply.domain.assignment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_assignment", columnNames = ["userId", "missionId"])
    ]
)
@Entity
class Assignment(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false)
    var githubUsername: String,

    @Column(nullable = false)
    var pullRequestUrl: String,

    @Lob
    var note: String,
    id: Long = 0L
) : BaseEntity(id) {
    fun update(githubUsername: String, pullRequestUrl: String, note: String) {
        this.githubUsername = githubUsername
        this.pullRequestUrl = pullRequestUrl
        this.note = note
    }
}
