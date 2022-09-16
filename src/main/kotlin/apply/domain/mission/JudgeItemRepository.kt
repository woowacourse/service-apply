package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository

interface JudgeItemRepository : JpaRepository<JudgeItem, Long> {
    fun findByMissionId(missionId: Long): JudgeItem?
}
