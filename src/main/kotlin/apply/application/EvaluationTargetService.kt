package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class EvaluationTargetService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun getById(id: Long): EvaluationTarget = evaluationTargetRepository.findByIdOrNull(id)
        ?: throw IllegalArgumentException("EvaluationTarget (id=$id) 가 존재하지 않습니다")

    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget> =
        evaluationTargetRepository.findAllByEvaluationId(evaluationId)

    fun findAllByEvaluationIdAndKeyword(
        evaluationId: Long,
        keyword: String = ""
    ): List<EvaluationTargetResponse> {
        val evaluationTargets = findAllByEvaluationId(evaluationId)
        val applicants = applicantRepository.findAllByKeyword(keyword)

        return evaluationTargets
            .filter { applicants.any { applicant -> applicant.id == it.applicantId } }
            .map {
                val applicant = applicants.first { each -> each.id == it.applicantId }
                EvaluationTargetResponse(
                    it.id,
                    applicant.name,
                    applicant.email,
                    it.evaluationAnswers.countTotalScore(),
                    it.evaluationStatus,
                    it.administratorId,
                    it.note,
                    it.evaluationAnswers
                )
            }
    }

    fun load(evaluationId: Long) {
        val evaluation = evaluationRepository.findByIdOrNull(evaluationId) ?: throw IllegalArgumentException()
        val updatingApplicantIds = createUpdatingEvaluationTargets(evaluation)
            .map { it.applicantId }
            .toSet()
        val cheaterApplicantIds = applicantRepository.findAllByEmailIn(cheaterRepository.findAll().map { it.email })
            .filter { updatingApplicantIds.contains(it.id) }
            .map { it.id }
            .toSet()
        val currentApplicantIds = evaluationTargetRepository.findAllByEvaluationId(evaluationId)
            .map { it.applicantId }
            .toSet()

        val droppedApplicantIds = currentApplicantIds - updatingApplicantIds
        evaluationTargetRepository.deleteByEvaluationIdAndApplicantIdIn(evaluationId, droppedApplicantIds)

        val currentCheaterIds = currentApplicantIds intersect cheaterApplicantIds
        updateFail(currentCheaterIds, evaluation)

        val newWaitingApplicantIds = updatingApplicantIds - (currentApplicantIds + cheaterApplicantIds)
        save(newWaitingApplicantIds, evaluation, EvaluationStatus.WAITING)

        val newCheaterIds = cheaterApplicantIds - currentCheaterIds
        save(newCheaterIds, evaluation, EvaluationStatus.FAIL)
    }

    private fun createUpdatingEvaluationTargets(evaluation: Evaluation): List<EvaluationTarget> {
        return if (evaluation.hasBeforeEvaluation()) {
            createEvaluationTargets(evaluation)
        } else {
            createEvaluationTargetsFromRecruitment(evaluation)
        }
    }

    private fun createEvaluationTargets(evaluation: Evaluation): List<EvaluationTarget> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluation.beforeEvaluationId)
            .filter { it.isPassed }
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.applicantId) }
    }

    private fun createEvaluationTargetsFromRecruitment(evaluation: Evaluation): List<EvaluationTarget> {
        val applicantIds = applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(evaluation.recruitmentId)
            .map { it.applicantId }

        return applicantRepository.findAllById(applicantIds)
            .map { EvaluationTarget(evaluationId = evaluation.id, applicantId = it.id) }
    }

    private fun save(applicantIds: Set<Long>, evaluation: Evaluation, evaluationStatus: EvaluationStatus) {
        val evaluationTargets = applicantRepository.findAllById(applicantIds)
            .map {
                EvaluationTarget(
                    evaluationId = evaluation.id,
                    applicantId = it.id,
                    evaluationStatus = evaluationStatus
                )
            }
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    private fun updateFail(applicantIds: Set<Long>, evaluation: Evaluation) {
        evaluationTargetRepository.findAllByEvaluationIdAndApplicantIdIn(evaluation.id, applicantIds).forEach {
            it.evaluationStatus = EvaluationStatus.FAIL
        }
    }

    fun getGradeEvaluation(targetId: Long): GradeEvaluationResponse {
        val evaluationTarget = getById(targetId)
        val evaluation = evaluationRepository.findByIdOrNull(evaluationTarget.evaluationId)
            ?: throw IllegalArgumentException("EvaluationTarget (id=$targetId)의 Evaluation (id=${evaluationTarget.evaluationId}가 존재하지 않습니다")
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluation.id)

        val evaluationItemScores = evaluationItems.map {
            EvaluationItemScoreData(
                id = it.id,
                score = evaluationTarget.evaluationAnswers.findScoreByEvaluationItemId(it.id)
            )
        }

        return GradeEvaluationResponse(
            title = evaluation.title,
            description = evaluation.description,
            evaluationTarget = EvaluationTargetData(
                note = evaluationTarget.note,
                evaluationStatus = evaluationTarget.evaluationStatus,
                evaluationItemScores = evaluationItemScores
            ),
            evaluationItems = evaluationItems.map { EvaluationItemResponse(it) }
        )
    }

    fun grade(evaluationTargetId: Long, request: EvaluationTargetData) {
        val evaluationTarget = getById(evaluationTargetId)

        val evaluationAnswers = request.evaluationItemScores
            .map { EvaluationAnswer(it.score, it.id) }
            .toMutableList()
        evaluationTarget.update(
            evaluationStatus = request.evaluationStatus,
            evaluationAnswers = EvaluationAnswers(evaluationAnswers),
            note = request.note
        )
    }
}
