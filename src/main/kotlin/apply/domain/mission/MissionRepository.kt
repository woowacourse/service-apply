package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository

interface MissionRepository : JpaRepository<Mission, Long> {
    fun existsByEvaluationId(evaluationId: Long): Boolean
}
