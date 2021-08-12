package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.domain.term.Term
import apply.domain.term.TermRepository
import apply.domain.term.getById
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class RecruitmentService(
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val evaluationRepository: EvaluationRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val termRepository: TermRepository
) {
    fun save(request: RecruitmentData) {
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
            findRecruitmentItems(request.id, request.recruitmentItems.map { it.id })
        )
        recruitmentItemRepository.saveAll(
            request.recruitmentItems.map {
                RecruitmentItem(recruitment.id, it.title, it.position, it.maximumLength, it.description, it.id)
            }
        )
    }

    private fun findRecruitmentItems(recruitmentId: Long, excludedItemIds: List<Long>): List<RecruitmentItem> {
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
        validateDeletable(recruitment)
        recruitmentItemRepository.deleteInBatch(findRecruitmentItems(id))
        val evaluations = evaluationRepository.findAllByRecruitmentId(id)
        deleteEvaluationItems(evaluations)
        evaluationRepository.deleteInBatch(evaluations)
        recruitmentRepository.delete(recruitment)
    }

    private fun deleteEvaluationItems(evaluations: List<Evaluation>) {
        evaluations.forEach {
            val findAllByEvaluationId = findAllEvaluationItems(it)
            evaluationItemRepository.deleteInBatch(findAllByEvaluationId)
        }
    }

    private fun findAllEvaluationItems(evaluation: Evaluation): List<EvaluationItem> =
        evaluationItemRepository.findAllByEvaluationId(evaluation.id)

    private fun findRecruitmentItems(id: Long): List<RecruitmentItem> =
        recruitmentItemRepository.findAllByRecruitmentId(id)

    private fun validateDeletable(recruitment: Recruitment) {
        check(!recruitment.recruitable)
        check(!applicationFormRepository.existsByRecruitmentId(recruitment.id))
    }

    fun getById(id: Long): Recruitment =
        recruitmentRepository.findByIdOrNull(id) ?: throw IllegalArgumentException()

    fun getNotEndedDataById(id: Long): RecruitmentData {
        val recruitment = getById(id)
        val term = termRepository.getById(recruitment.termId)
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitment.id)
        return RecruitmentData(recruitment, term, recruitmentItems)
    }

    fun findAllTermSelectData(): List<TermSelectData> {
        val terms = listOf(Term.SINGLE) + termRepository.findAll().sortedBy { it.name }
        return terms.map(::TermSelectData)
    }
}
