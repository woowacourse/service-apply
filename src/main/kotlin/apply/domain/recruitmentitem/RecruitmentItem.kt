package apply.domain.recruitmentitem

import apply.domain.recruitment.Recruitment
import javax.persistence.Column
import javax.persistence.ConstraintMode
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class RecruitmentItem(
    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @ManyToOne
    @JoinColumn(name = "RECRUITMENT_ID", foreignKey = ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    var recruitment: Recruitment,

    @Column(nullable = false)
    var maximumLength: Int = 1000,

    @Column(nullable = false, name = "ORDER_COLUMN")
    var order: Int = 0,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
