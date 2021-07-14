package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.RecruitmentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class EvaluationService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val recruitmentRepository: RecruitmentRepository
) {
    fun save(request: EvaluationData) {
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
    }

    private fun findEvaluationItemsToDelete(evaluationId: Long, excludedItemIds: List<Long>): List<EvaluationItem> {
        return evaluationItemRepository
            .findByEvaluationIdOrderByPosition(evaluationId)
            .filterNot { excludedItemIds.contains(it.id) }
    }

    fun findAll(): List<Evaluation> {
        return evaluationRepository.findAll()
    }

    fun findAllByRecruitmentId(recruitmentId: Long): List<Evaluation> {
        return evaluationRepository.findAllByRecruitmentId(recruitmentId)
    }

    fun findById(id: Long): Evaluation? {
        if (id == 0L) return null

        return evaluationRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("해당 id의 평가를 찾을 수 없습니다.")
    }

    fun findAllRecruitmentSelectData(): List<RecruitmentSelectData> {
        return recruitmentRepository.findAll()
            .map { recruitment -> RecruitmentSelectData(recruitment) }
            .sortedByDescending { it.id }
    }

    fun getDataById(id: Long): EvaluationData {
        val evaluation = findById(id) ?: throw IllegalArgumentException("해당 id의 평가를 찾을 수 없습니다.")
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluation.id)
        val recruitment = recruitmentRepository.getOne(evaluation.recruitmentId)
        val beforeEvaluation = findById(evaluation.beforeEvaluationId)

        return EvaluationData(evaluation, recruitment, beforeEvaluation, evaluationItems)
    }

    fun getAllSelectDataByRecruitmentId(id: Long): List<EvaluationSelectData> {
        return findAllByRecruitmentId(id).map { EvaluationSelectData(it) }
    }

    fun findAllWithRecruitment(): List<EvaluationResponse> {
        return findAll().map {
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

    fun deleteById(evaluationId: Long) {
        evaluationRepository.deleteById(evaluationId)

        resetBeforeEvaluationContain(evaluationId)
    }

    private fun resetBeforeEvaluationContain(id: Long) {
        evaluationRepository.findAll()
            .filter { it.hasSameBeforeEvaluationWith(id) }
            .forEach { it.resetBeforeEvaluation() }
    }
}
