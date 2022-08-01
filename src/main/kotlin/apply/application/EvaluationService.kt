package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getById
import org.springframework.stereotype.Service
import javax.transaction.Transactional

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
        return EvaluationResponse(
            evaluation,
            request.recruitment.title,
            request.recruitment.id,
            request.beforeEvaluation.title,
            request.beforeEvaluation.id
        )
    }

    private fun findEvaluationItemsToDelete(evaluationId: Long, excludedItemIds: List<Long>): List<EvaluationItem> {
        return evaluationItemRepository
            .findByEvaluationIdOrderByPosition(evaluationId)
            .filterNot { excludedItemIds.contains(it.id) }
    }

    fun getById(id: Long): EvaluationResponse {
        val evaluation = evaluationRepository.getById(id)
        val recruitment = recruitmentRepository.getById(evaluation.recruitmentId)
        val beforeEvaluation = findById(evaluation.beforeEvaluationId)
        return EvaluationResponse(
            evaluation,
            recruitment.title,
            recruitment.id,
            beforeEvaluation?.title ?: "이전 평가 없음",
            beforeEvaluation?.id ?: 0
        )
    }

    fun findAllWithRecruitment(): List<EvaluationResponse> {
        return evaluationRepository.findAll().map {
            EvaluationResponse(
                it.id,
                it.title,
                it.description,
                recruitmentRepository.getOne(it.recruitmentId).title,
                it.recruitmentId,
                findById(it.beforeEvaluationId)?.title ?: "이전 평가 없음",
                it.beforeEvaluationId
            )
        }
    }

    fun findById(id: Long): Evaluation? {
        if (id == 0L) return null
        return evaluationRepository.getById(id)
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

    fun findAllByRecruitmentId(recruitmentId: Long): List<Evaluation> {
        return evaluationRepository.findAllByRecruitmentId(recruitmentId)
    }

    fun getDataById(id: Long): EvaluationData {
        val evaluation = evaluationRepository.getById(id)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluation.id)
        val recruitment = recruitmentRepository.getOne(evaluation.recruitmentId)
        val beforeEvaluation = findById(evaluation.beforeEvaluationId)
        return EvaluationData(evaluation, recruitment, beforeEvaluation, evaluationItems)
    }

    fun findAllRecruitmentSelectData(): List<RecruitmentSelectData> {
        return recruitmentRepository.findAll()
            .map(::RecruitmentSelectData)
            .sortedByDescending { it.id }
    }

    fun getAllSelectDataByRecruitmentId(id: Long): List<EvaluationSelectData> {
        return findAllByRecruitmentId(id).map { EvaluationSelectData(it) }
    }
}
