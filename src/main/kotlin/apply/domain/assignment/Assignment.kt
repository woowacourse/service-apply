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
    val gitAccount: String,

    @Column(nullable = false)
    val url: String,

    @Lob
    val impression: String,

    id: Long = 0L
) : BaseEntity(id)
