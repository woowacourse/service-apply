package apply.domain.mission

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class JudgmentItem(
    @Column(nullable = false)
    val missionId: Long = 0L,

    @Column(nullable = false)
    var evaluationItemId: Long = 0L,

    @Column(nullable = false)
    var testName: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var programmingLanguage: ProgrammingLanguage = ProgrammingLanguage.NONE,
    id: Long = 0L
) : BaseEntity(id)
