package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun MissionRepository.getByEvaluationId(evaluationId: Long): Mission = findByEvaluationId(evaluationId)
    ?: throw NoSuchElementException("과제가 존재하지 않습니다. evaluationId: $evaluationId")

fun MissionRepository.getById(id: Long): Mission = findByIdOrNull(id)
    ?: throw NoSuchElementException("과제가 존재하지 않습니다. id: $id")

interface MissionRepository : JpaRepository<Mission, Long> {
    fun existsByEvaluationId(evaluationId: Long): Boolean
    fun findByEvaluationId(evaluationId: Long): Mission?
    fun findAllByEvaluationIdIn(evaluationIds: Collection<Long>): List<Mission>
}
