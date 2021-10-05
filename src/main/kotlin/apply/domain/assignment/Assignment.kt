package apply.domain.assignment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_assignment", columnNames = ["missionId", "applicantId"])
    ]
)
@Entity
class Assignment(
    @Column(nullable = false)
    val applicantId: Long,

    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false)
    val githubUsername: String,

    @Column(nullable = false)
    val pullRequestUrl: String,

    @Lob
    val note: String,

    id: Long = 0L
) : BaseEntity(id)
