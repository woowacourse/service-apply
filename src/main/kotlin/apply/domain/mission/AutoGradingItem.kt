package apply.domain.mission

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class AutoGradingItem(
    @Column
    val missionId: Long,
    @Column
    val evaluationItemId: Long,
    @Column
    val gradingName: String,
    @Enumerated(EnumType.STRING)
    val programmingLanguage: ProgramingLanguage,

    id: Long = 0L
) : BaseEntity(id)
