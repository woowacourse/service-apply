package apply.domain.recruitmentitem

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class RecruitmentItem(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val recruitmentId: Long,

    @Column(nullable = false)
    val maximumLength: Int = 1000,

    @Column(nullable = false)
    val position: Int = 0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
