package apply.domain.recruitmentitem

import org.springframework.data.jpa.repository.JpaRepository

interface RecruitmentItemRepository : JpaRepository<RecruitmentItem, Long> {
    fun findByRecruitmentIdOrderByOrder(recruitmentId: Long): List<RecruitmentItem>
}
