package apply.domain.assignment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class Assignment(
    @Column(nullable = false)
    val applicantId: Long,

    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false)
    val githubId: String,

    @Column(nullable = false)
    val url: String,

    @Lob
    val impression: String,

    id: Long = 0L
) : BaseEntity(id)
