package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun MissionRepository.getById(missionId: Long): Mission {
    return findByIdOrNull(missionId) ?: throw NoSuchElementException()
}

interface MissionRepository : JpaRepository<Mission, Long> {
    fun existsByEvaluationId(evaluationId: Long): Boolean
    fun findAllByEvaluationIdIn(evaluationIds: Collection<Long>): List<Mission>
}
