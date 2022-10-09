package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationitem.EvaluationItem
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.recruitment.RecruitmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class EvaluationService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val recruitmentRepository: RecruitmentRepository
) {
    fun save(request: EvaluationData): EvaluationResponse {
        val evaluation = evaluationRepository.save(
            Evaluation(
                request.title,
                request.description,
                request.recruitment.id,
                request.beforeEvaluation.id,
                request.id
            )
        )
        evaluationItemRepository.deleteAll(
            findEvaluationItemsToDelete(request.id, request.evaluationItems.map { it.id })
        )
        evaluationItemRepository.saveAll(
            request.evaluationItems.map {
                EvaluationItem(it.title, it.description, it.maximumScore, it.position, evaluation.id, it.id)
            }
        )
        return EvaluationResponse(evaluation)
    }

    private fun findEvaluationItemsToDelete(evaluationId: Long, excludedItemIds: List<Long>): List<EvaluationItem> {
        return evaluationItemRepository
            .findByEvaluationIdOrderByPosition(evaluationId)
            .filterNot { excludedItemIds.contains(it.id) }
    }

    fun getById(id: Long): EvaluationResponse {
        return evaluationRepository.getById(id).let(::EvaluationResponse)
    }

    fun findAllWithRecruitment(): List<EvaluationGridResponse> {
        val evaluations = evaluationRepository.findAll()
        val recruitmentsById = recruitmentRepository
            .findAllById(evaluations.map { it.recruitmentId })
            .associateBy { it.id }
        val evaluationsById = evaluations.associateBy { it.id }
        return evaluations.map {
            EvaluationGridResponse(
                it,
                recruitmentsById.getValue(it.recruitmentId),
                evaluationsById[it.beforeEvaluationId]
            )
        }
    }

    fun deleteById(id: Long) {
        evaluationRepository.deleteById(id)
        resetBeforeEvaluationContain(id)
    }

    private fun resetBeforeEvaluationContain(id: Long) {
        evaluationRepository.findAll()
            .filter { it.hasSameBeforeEvaluationWith(id) }
            .forEach { it.resetBeforeEvaluation() }
    }

    fun getDataById(id: Long): EvaluationData {
        val evaluation = evaluationRepository.getById(id)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluation.id)
        val recruitment = recruitmentRepository.getOne(evaluation.recruitmentId)
        val beforeEvaluation = findById(evaluation.beforeEvaluationId)
        return EvaluationData(evaluation, recruitment, beforeEvaluation, evaluationItems)
    }

    private fun findById(id: Long): Evaluation? {
        if (id == 0L) return null
        return evaluationRepository.getById(id)
    }

    fun findAllRecruitmentSelectData(): List<RecruitmentSelectData> {
        return recruitmentRepository.findAll()
            .map(::RecruitmentSelectData)
            .sortedByDescending { it.id }
    }

    fun getAllSelectDataByRecruitmentId(recruitmentId: Long): List<EvaluationSelectData> {
        return evaluationRepository.findAllByRecruitmentId(recruitmentId).map(::EvaluationSelectData)
    }
}
