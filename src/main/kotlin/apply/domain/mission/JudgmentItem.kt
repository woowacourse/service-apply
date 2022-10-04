package apply.domain.mission

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class JudgmentItem(
    @OneToOne(optional = false)
    @JoinColumn
    val mission: Mission,

    @Column(nullable = false)
    var evaluationItemId: Long = 0L,

    @Column(nullable = false)
    var testName: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var language: Language = Language.NONE
) : BaseEntity()
