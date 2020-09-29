package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Transactional
@Service
class EvaluationTargetService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationRepository: EvaluationRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findByEvaluationId(evaluationId: Long): List<EvaluationTarget> =
        evaluationTargetRepository.findByEvaluationId(evaluationId)

    fun update(evaluationId: Long) {
        val evaluation = evaluationRepository.findByIdOrNull(evaluationId) ?: throw IllegalArgumentException()
        val cachedCheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }

        if (evaluation.hasBeforeEvaluation()) {
            updateEvaluationTarget(evaluation, cachedCheaterApplicantIds)
        } else {
            updateEvaluationTargetOfFirst(evaluation, cachedCheaterApplicantIds)
        }
    }

    private fun updateEvaluationTargetOfFirst(evaluation: Evaluation, cachedCheaterApplicantIds: List<Long>) {
        val oldApplicantIds = evaluationTargetRepository.findByEvaluationId(evaluation.id)
            .map { it.applicantId }
            .toSet()

        val newApplicantIds = createEvaluationTargetsFromFirst(evaluation)
            .map { it.applicantId }
            .filter { !cachedCheaterApplicantIds.contains(it) }
            .toSet()

        val cheaterApplicantIds = (oldApplicantIds + newApplicantIds).filter { cachedCheaterApplicantIds.contains(it) }

        if (isAlreadyUpToDate(oldApplicantIds, cheaterApplicantIds, newApplicantIds)) {
            delete(evaluation, cheaterApplicantIds)
        } else {
            deleteAndSave(oldApplicantIds, newApplicantIds, cheaterApplicantIds, evaluation)
        }
    }

    private fun createEvaluationTargetsFromFirst(evaluation: Evaluation): List<EvaluationTarget> {
        val applicantIds = applicationFormRepository.findByRecruitmentId(evaluation.recruitmentId)
            .map { it.applicantId }

        return applicantRepository.findAllById(applicantIds)
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }
    }

    private fun isAlreadyUpToDate(
        oldApplicantIds: Set<Long>,
        cheaterApplicantIds: List<Long>,
        newApplicantIds: Set<Long>
    ): Boolean {
        val oldNormalApplicantIds = oldApplicantIds - cheaterApplicantIds
        val newNormalApplicantIds = newApplicantIds - cheaterApplicantIds

        return oldNormalApplicantIds.containsAll(newNormalApplicantIds) &&
            newNormalApplicantIds.containsAll(oldNormalApplicantIds)
    }

    private fun delete(evaluation: Evaluation, cheaterApplicantIds: List<Long>) {
        evaluationTargetRepository.deleteByEvaluationIdAndApplicantIdIn(evaluation.id, cheaterApplicantIds)
    }

    private fun deleteAndSave(
        oldApplicantIds: Set<Long>,
        newApplicantIds: Set<Long>,
        cheaterApplicantIds: List<Long>,
        evaluation: Evaluation
    ) {
        val deletingApplicantIds = oldApplicantIds - newApplicantIds + cheaterApplicantIds

        evaluationTargetRepository.deleteByEvaluationIdAndApplicantIdIn(evaluation.id, deletingApplicantIds)

        val addingApplicantIds = newApplicantIds - oldApplicantIds
        val additionalEvaluationTargets = applicantRepository.findAllById(addingApplicantIds)
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }

        evaluationTargetRepository.saveAll(additionalEvaluationTargets)
    }

    private fun updateEvaluationTarget(evaluation: Evaluation, cachedCheaterApplicantIds: List<Long>) {
        val oldApplicantIds = evaluationTargetRepository.findByEvaluationId(evaluation.id)
            .map { it.applicantId }
            .toSet()

        val newApplicantIds = createEvaluationTargetsFrom(evaluation)
            .map { it.applicantId }
            .filter { !cachedCheaterApplicantIds.contains(it) }
            .toSet()

        val cheaterApplicantIds = (oldApplicantIds + newApplicantIds).filter { cachedCheaterApplicantIds.contains(it) }

        if (isAlreadyUpToDate(oldApplicantIds, cheaterApplicantIds, newApplicantIds)) {
            delete(evaluation, cheaterApplicantIds)
        } else {
            deleteAndSave(oldApplicantIds, newApplicantIds, cheaterApplicantIds, evaluation)
        }
    }

    private fun createEvaluationTargetsFrom(evaluation: Evaluation): List<EvaluationTarget> {
        return evaluationTargetRepository.findByEvaluationId(evaluation.beforeEvaluationId)
            .filter { it.isPassed }
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.applicantId) }
    }

    @PostConstruct
    private fun populateDummy() {
        if (evaluationTargetRepository.count() != 0L) {
            return
        }
        val evaluationTargets = listOf(
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 1L,
                applicantId = 1L
            ),
            EvaluationTarget(
                evaluationId = 1L,
                administratorId = 1L,
                applicantId = 2L
            ),
            EvaluationTarget(
                evaluationId = 2L,
                administratorId = 1L,
                applicantId = 3L
            )
        )
        evaluationTargetRepository.saveAll(evaluationTargets)
    }
}
