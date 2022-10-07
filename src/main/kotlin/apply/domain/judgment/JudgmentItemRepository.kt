package apply.domain.judgment

import org.springframework.data.jpa.repository.JpaRepository

fun JudgmentItemRepository.getByMissionId(missionId: Long): JudgmentItem {
    return findByMissionId(missionId) ?: throw NoSuchElementException("예제 테스트를 실행할 수 없습니다.")
}

interface JudgmentItemRepository : JpaRepository<JudgmentItem, Long> {
    fun findByMissionId(missionId: Long): JudgmentItem?
}
