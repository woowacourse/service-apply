package apply.domain.judgmentitem

import org.springframework.data.jpa.repository.JpaRepository

fun JudgmentItemRepository.getByMissionId(missionId: Long): JudgmentItem = findByMissionId(missionId)
    ?: throw NoSuchElementException("자동 채점 항목이 존재하지 않습니다. missionId: $missionId")

interface JudgmentItemRepository : JpaRepository<JudgmentItem, Long> {
    fun findByMissionId(missionId: Long): JudgmentItem?
    fun findAllByMissionIdIn(missionIds: Collection<Long>): List<JudgmentItem>
    fun deleteByMissionId(missionId: Long)
    fun existsByMissionId(missionId: Long): Boolean
}
