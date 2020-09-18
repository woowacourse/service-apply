package apply.domain.recruitmentitem

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob

@Entity
class RecruitmentItem(
    @Column(nullable = false)
    val recruitmentId: Long,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val position: Int,

    @Column(nullable = false)
    val maximumLength: Int,

    @Column(nullable = false)
    @Lob
    val description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
