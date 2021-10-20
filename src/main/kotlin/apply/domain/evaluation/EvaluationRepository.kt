package apply.domain.evaluation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun EvaluationRepository.getById(id: Long): Evaluation = findByIdOrNull(id)
    ?: throw NoSuchElementException("평가가 존재하지 않습니다. id: $id")

interface EvaluationRepository : JpaRepository<Evaluation, Long> {
    fun findAllByRecruitmentId(recruitmentId: Long): List<Evaluation>
}
