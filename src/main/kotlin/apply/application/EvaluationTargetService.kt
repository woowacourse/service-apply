package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationStatus
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

    fun load(evaluationId: Long): List<EvaluationTarget> {
        val evaluation: Evaluation =
            evaluationRepository.findByIdOrNull(evaluationId) ?: throw IllegalArgumentException()
        val isSavedBefore: Boolean = evaluationTargetRepository.existsByEvaluationId(evaluationId)

        return when {
            !evaluation.hasBeforeEvaluation() && !isSavedBefore -> saveEvaluationTargetOfFirst(evaluation)
            evaluation.hasBeforeEvaluation() && !isSavedBefore -> saveEvaluationTarget(evaluation)
            !evaluation.hasBeforeEvaluation() && isSavedBefore -> updateEvaluationTargetOfFirst(evaluation)
            evaluation.hasBeforeEvaluation() && isSavedBefore -> updateEvaluationTarget(evaluation)
            else -> throw IllegalStateException("평가 대상자 리스트를 불러올 수 없습니다.")
        }
    }

    private fun saveEvaluationTargetOfFirst(evaluation: Evaluation): MutableList<EvaluationTarget> {
        val evaluationTargets: List<EvaluationTarget> = createEvaluationTargetsFromFirst(evaluation)

        return evaluationTargetRepository.saveAll(evaluationTargets)
    }

    private fun createEvaluationTargetsFromFirst(evaluation: Evaluation): List<EvaluationTarget> {
        val applicantIds: List<Long> =
            applicationFormRepository.findByRecruitmentId(evaluation.recruitmentId).map { it.applicantId }

        return applicantRepository.findAllById(applicantIds)
            .filter { isNotCheater(it.id) }
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }
    }

    private fun isNotCheater(applicantId: Long) = !cheaterRepository.existsByApplicantId(applicantId)

    private fun saveEvaluationTarget(evaluation: Evaluation): MutableList<EvaluationTarget> {
        val evaluationTargets = createEvaluationTargetsFrom(evaluation)

        return evaluationTargetRepository.saveAll(evaluationTargets)
    }

    private fun createEvaluationTargetsFrom(evaluation: Evaluation): List<EvaluationTarget> {
        return evaluationTargetRepository.findByEvaluationId(evaluation.beforeEvaluationId)
            .filter { EvaluationStatus.PASS.equals(it.evaluationStatus) }
            .filter { isNotCheater(it.applicantId) }
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.applicantId) }
    }

    private fun updateEvaluationTargetOfFirst(evaluation: Evaluation): List<EvaluationTarget> {
        val persistApplicantIds: Set<Long> =
            evaluationTargetRepository.findByEvaluationId(evaluation.id).map { it.applicantId }.toSet()

        val newApplicantIds: Set<Long> = createEvaluationTargetsFromFirst(evaluation).map { it.applicantId }.toSet()

        update(persistApplicantIds, newApplicantIds, evaluation)

        return evaluationTargetRepository.findByEvaluationId(evaluation.id)
    }

    private fun update(
        persistApplicantIds: Set<Long>,
        newApplicantIds: Set<Long>,
        evaluation: Evaluation
    ) {
        val deletingApplicantIds = persistApplicantIds subtract newApplicantIds

        evaluationTargetRepository.deleteByEvaluationIdAndApplicantIdIn(evaluation.id, deletingApplicantIds)

        val addingApplicantIds = newApplicantIds subtract persistApplicantIds

        val additionalEvaluationTargets: List<EvaluationTarget> =
            applicantRepository.findAllById(addingApplicantIds)
                .filter { isNotCheater(it.id) }
                .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }

        evaluationTargetRepository.saveAll(additionalEvaluationTargets)
    }

    private fun updateEvaluationTarget(evaluation: Evaluation): List<EvaluationTarget> {
        val persistApplicantIds =
            evaluationTargetRepository.findByEvaluationId(evaluation.id).map { it.applicantId }.toSet()

        val newApplicantIds = createEvaluationTargetsFrom(evaluation).map { it.applicantId }.toSet()

        update(persistApplicantIds, newApplicantIds, evaluation)

        return evaluationTargetRepository.findByEvaluationId(evaluation.id)
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
