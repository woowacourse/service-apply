package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun MissionRepository.getById(missionId: Long): Mission {
    return findByIdOrNull(missionId) ?: throw NoSuchElementException("해당 과제는 존재하지 않습니다.")
}

interface MissionRepository : JpaRepository<Mission, Long> {
    fun existsByEvaluationId(evaluationId: Long): Boolean
    fun findByEvaluationId(evaluationId: Long): Mission?
    fun findAllByEvaluationIdIn(evaluationIds: Collection<Long>): List<Mission>
}
