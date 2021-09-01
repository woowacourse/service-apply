package apply.domain.recruitmentitem

import org.springframework.data.jpa.repository.JpaRepository

interface RecruitmentItemRepository : JpaRepository<RecruitmentItem, Long> {
    fun findByRecruitmentIdOrderByPosition(recruitmentId: Long): List<RecruitmentItem>
}
