package apply.domain.judgmentitem

import org.springframework.data.jpa.repository.JpaRepository

fun JudgmentItemRepository.getByMissionId(missionId: Long): JudgmentItem =
    findByMissionId(missionId) ?: throw NoSuchElementException()

interface JudgmentItemRepository : JpaRepository<JudgmentItem, Long> {
    fun findByMissionId(missionId: Long): JudgmentItem?
    fun deleteByMissionId(missionId: Long)
    fun existsByMissionId(missionId: Long): Boolean
}
