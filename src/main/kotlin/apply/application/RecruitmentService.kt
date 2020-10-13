package apply.application

import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDateTime
import javax.annotation.PostConstruct

@Transactional
@Service
class RecruitmentService(
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository
) {
    fun save(request: RecruitmentData) {
        val recruitment = recruitmentRepository.save(
            Recruitment(
                request.title,
                request.startDateTime,
                request.endDateTime,
                request.canRecruit,
                request.isHidden,
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
        return recruitmentRepository.findAllByIsHiddenFalse().map(::RecruitmentResponse)
    }

    fun deleteById(id: Long) {
        val recruitment = getById(id)
        check(!recruitment.canRecruit)
        recruitmentRepository.delete(recruitment)
    }

    fun getById(id: Long): Recruitment =
        recruitmentRepository.findByIdOrNull(id) ?: throw IllegalArgumentException()

    fun getNotEndedDataById(id: Long): RecruitmentData {
        val recruitment = getById(id)
        check(!recruitment.isEnded) { "모집이 이미 완료되었습니다." }
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitment.id)
        return RecruitmentData(recruitment, recruitmentItems)
    }

    @PostConstruct
    private fun populateDummy() {
        if (recruitmentRepository.count() != 0L) {
            return
        }
        val recruitments = listOf(
            Recruitment(
                title = "지원할 제목",
                startDateTime = createLocalDateTime(2020, 10, 5, 10),
                endDateTime = createLocalDateTime(2020, 11, 5, 10),
                canRecruit = true,
                isHidden = false
            ),
            Recruitment(
                title = "웹 백엔드 2기",
                startDateTime = createLocalDateTime(2019, 10, 25, 10),
                endDateTime = createLocalDateTime(2019, 11, 5, 10),
                canRecruit = true,
                isHidden = false
            ),
            Recruitment(
                title = "웹 백엔드 3기",
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10),
                canRecruit = true,
                isHidden = true
            ),
            Recruitment(
                title = "웹 프론트엔드 3기",
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10),
                canRecruit = false,
                isHidden = false
            )
        )
        recruitmentRepository.saveAll(recruitments)
    }
}
