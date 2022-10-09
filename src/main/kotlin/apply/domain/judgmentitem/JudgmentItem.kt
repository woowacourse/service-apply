package apply.domain.judgmentitem

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class JudgmentItem(
    @Column(unique = true, nullable = false)
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
        validate(evaluationItemId, testName, programmingLanguage)
    }

    fun update(evaluationItemId: Long, testName: String, programmingLanguage: ProgrammingLanguage) {
        validate(evaluationItemId, testName, programmingLanguage)
        this.evaluationItemId = evaluationItemId
        this.testName = testName
        this.programmingLanguage = programmingLanguage
    }

    private fun validate(evaluationItemId: Long, testName: String, programmingLanguage: ProgrammingLanguage) {
        require(
            evaluationItemId != 0L &&
                testName.isNotBlank() &&
                programmingLanguage != ProgrammingLanguage.NONE
        ) { "자동 채점 평가 항목이 올바르지 않습니다." }
    }
}
