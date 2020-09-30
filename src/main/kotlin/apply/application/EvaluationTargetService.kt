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

    fun load(evaluationId: Long) {
        val evaluation = evaluationRepository.findByIdOrNull(evaluationId) ?: throw IllegalArgumentException()
        val cheaterApplicantIds = cheaterRepository.findAll().map { it.applicantId }

        val updatingEvaluationTargets =
            createUpdatingEvaluationTargets(evaluation).filterNot { cheaterApplicantIds.contains(it.applicantId) }

        val updatingApplicantIds = updatingEvaluationTargets.map { it.applicantId }.toSet()

        val currentApplicantIds =
            evaluationTargetRepository.findByEvaluationId(evaluationId).map { it.applicantId }.toSet()

        evaluationTargetRepository.deleteByEvaluationIdAndApplicantIdIn(
            evaluationId,
            currentApplicantIds - updatingApplicantIds
        )

        val newApplicantIds = updatingApplicantIds - currentApplicantIds

        save(newApplicantIds, evaluation)
    }

    private fun createUpdatingEvaluationTargets(evaluation: Evaluation): List<EvaluationTarget> {
        return if (evaluation.hasBeforeEvaluation()) {
            createEvaluationTargets(evaluation)
        } else {
            createEvaluationTargetsFromRecruitment(evaluation)
        }
    }

    private fun createEvaluationTargets(evaluation: Evaluation): List<EvaluationTarget> {
        return evaluationTargetRepository.findByEvaluationId(evaluation.beforeEvaluationId)
            .filter { it.isPassed }
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.applicantId) }
    }

    private fun createEvaluationTargetsFromRecruitment(evaluation: Evaluation): List<EvaluationTarget> {
        val applicantIds = applicationFormRepository.findByRecruitmentId(evaluation.recruitmentId)
            .map { it.applicantId }

        return applicantRepository.findAllById(applicantIds)
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }
    }

    private fun save(applicantIds: Set<Long>, evaluation: Evaluation) {
        val additionalEvaluationTargets = applicantRepository.findAllById(applicantIds)
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }

        evaluationTargetRepository.saveAll(additionalEvaluationTargets)
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
