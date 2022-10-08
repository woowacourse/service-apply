package apply.domain.mission.judgmentItem

import org.springframework.data.jpa.repository.JpaRepository

interface JudgmentItemRepository : JpaRepository<JudgmentItem, Long> {
    fun findByMissionId(missionId: Long): JudgmentItem?
    fun deleteByMissionId(missionId: Long)
}
