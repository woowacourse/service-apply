package apply.application

import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getById
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.domain.term.TermRepository
import apply.domain.term.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class RecruitmentService(
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val termRepository: TermRepository
) {
    fun save(request: RecruitmentData): Long {
        val recruitment = recruitmentRepository.save(
            Recruitment(
                request.title,
                request.startDateTime,
                request.endDateTime,
                request.term.id,
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
        return recruitment.id
    }

    private fun findRecruitmentItemsToDelete(recruitmentId: Long, excludedItemIds: List<Long>): List<RecruitmentItem> {
        return recruitmentItemRepository
            .findByRecruitmentIdOrderByPosition(recruitmentId)
            .filterNot { excludedItemIds.contains(it.id) }
    }

    fun findAll(): List<RecruitmentResponse> {
        return recruitmentRepository.findAll()
            .map { RecruitmentResponse(it, termRepository.getById(it.termId)) }
    }

    fun findAllNotHidden(): List<RecruitmentResponse> {
        return recruitmentRepository.findAllByHiddenFalse()
            .map { RecruitmentResponse(it, termRepository.getById(it.termId)) }
    }

    fun deleteById(id: Long) {
        val recruitment = getById(id)
        check(!recruitment.recruitable) { "모집 중인 모집은 삭제할 수 없습니다." }
        recruitmentRepository.delete(recruitment)
    }

    fun getById(id: Long): Recruitment = recruitmentRepository.getById(id)

    fun getNotEndedDataById(id: Long): RecruitmentData {
        val recruitment = getById(id)
        val term = termRepository.getById(recruitment.termId)
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitment.id)
        return RecruitmentData(recruitment, term, recruitmentItems)
    }
}
