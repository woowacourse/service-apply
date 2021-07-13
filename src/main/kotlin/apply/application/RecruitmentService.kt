package apply.application

import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class RecruitmentService(
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository
) {
    // TODO :: RecruitmentData term 뚫기
    fun save(request: RecruitmentData) {
        val recruitment = recruitmentRepository.save(
            Recruitment(
                request.title,
                request.startDateTime,
                request.endDateTime,
                request.recruitable,
                request.hidden,
                request.id
            )
        )
        recruitmentItemRepository.deleteAll(
            findRecruitmentItemsToDelete(request.id, request.recruitmentItems.map { it.id })
        )
        recruitmentItemRepository.saveAll(
            request.recruitmentItems.map {
                RecruitmentItem(recruitment.id, it.title, it.position, it.maximumLength, it.description, it.id)
            }
        )
    }

    private fun findRecruitmentItemsToDelete(recruitmentId: Long, excludedItemIds: List<Long>): List<RecruitmentItem> {
        return recruitmentItemRepository
            .findByRecruitmentIdOrderByPosition(recruitmentId)
            .filterNot { excludedItemIds.contains(it.id) }
    }

    fun findAll(): List<Recruitment> {
        return recruitmentRepository.findAll()
    }

    fun findAllNotHidden(): List<RecruitmentResponse> {
        return recruitmentRepository.findAllByHiddenFalse().map(::RecruitmentResponse)
    }

    fun deleteById(id: Long) {
        val recruitment = getById(id)
        check(!recruitment.recruitable)
        recruitmentRepository.delete(recruitment)
    }

    fun getById(id: Long): Recruitment =
        recruitmentRepository.findByIdOrNull(id) ?: throw IllegalArgumentException()

    fun getNotEndedDataById(id: Long): RecruitmentData {
        val recruitment = getById(id)
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitment.id)
        return RecruitmentData(recruitment, recruitmentItems)
    }
}
