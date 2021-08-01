package apply.domain.evaluationItem

import org.springframework.data.jpa.repository.JpaRepository

interface EvaluationItemRepository : JpaRepository<EvaluationItem, Long> {
    fun findByEvaluationIdOrderByPosition(evaluationId: Long): List<EvaluationItem>
    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationItem>
}
