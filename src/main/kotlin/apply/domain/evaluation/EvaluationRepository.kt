package apply.domain.evaluation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun EvaluationRepository.getById(evaluationId: Long): Evaluation {
    return findByIdOrNull(evaluationId) ?: throw NoSuchElementException()
}

interface EvaluationRepository : JpaRepository<Evaluation, Long> {
    fun findAllByRecruitmentId(recruitmentId: Long): List<Evaluation>
}
