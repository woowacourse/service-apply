package apply.domain.evaluationtarget

import org.springframework.data.jpa.repository.JpaRepository

interface EvaluationTargetRepository : JpaRepository<EvaluationTarget, Long> {
    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget>

    fun existsByEvaluationId(evaluationId: Long): Boolean

    fun deleteByApplicantIdIn(applicantIds: Collection<Long>)

    fun deleteByEvaluationIdAndApplicantIdIn(evaluationId: Long, applicantIds: Collection<Long>)
}
