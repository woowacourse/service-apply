package apply.domain.evaluationtarget

import org.springframework.data.jpa.repository.JpaRepository

interface EvaluationTargetRepository : JpaRepository<EvaluationTarget, Long> {
    fun findByEvaluationId(evaluationId: Long): List<EvaluationTarget>

    fun existsByEvaluationId(evaluationId: Long): Boolean
}
