package apply.application

import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class RecruitmentItemService(private val recruitmentItemItemRepository: RecruitmentItemRepository) {
    fun findByRecruitmentIdOrderByPosition(recruitmentId: Long): List<RecruitmentItem> {
        return recruitmentItemItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId)
    }
}
