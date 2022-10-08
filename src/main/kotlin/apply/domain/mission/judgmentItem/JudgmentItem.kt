package apply.domain.mission.judgmentItem

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class JudgmentItem(
    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false)
    var evaluationItemId: Long,

    @Column(nullable = false)
    var testName: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var programmingLanguage: ProgrammingLanguage,
    id: Long = 0L
) : BaseEntity(id) {
    init {
        require(evaluationItemId != 0L && testName.isNotEmpty() && programmingLanguage != ProgrammingLanguage.NONE) { "자동 채점 항목을 만들 수 없습니다." }
    }
}
