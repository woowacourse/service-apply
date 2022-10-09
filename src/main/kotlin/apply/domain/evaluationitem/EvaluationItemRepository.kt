package apply.domain.evaluationitem

import org.springframework.data.jpa.repository.JpaRepository

interface EvaluationItemRepository : JpaRepository<EvaluationItem, Long> {
    fun findByEvaluationIdOrderByPosition(evaluationId: Long): List<EvaluationItem>
}
