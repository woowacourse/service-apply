package apply.domain.mission

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class JudgmentItem(
    @Column
    val missionId: Long,

    @Column
    var evaluationItemId: Long?,

    @Column
    var testName: String?,

    @Enumerated(EnumType.STRING)
    var programmingLanguage: ProgrammingLanguage?,

    id: Long = 0L
) : BaseEntity(id)
