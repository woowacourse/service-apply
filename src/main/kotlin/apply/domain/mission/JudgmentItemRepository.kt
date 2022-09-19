package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository

interface JudgmentItemRepository : JpaRepository<JudgmentItem, Long> {
    fun findByMissionId(missionId: Long): JudgmentItem?
}
