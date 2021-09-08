package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailSendingService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val applicantRepository: ApplicantRepository,
) {
    fun findMailSendingTargetsByEvaluationStatus(
        evaluationId: Long,
        evaluationStatus: EvaluationStatus?
    ): List<MailSendingTargetResponse> {
        val targetApplicantIds = if (evaluationStatus == null) {
            evaluationTargetRepository.findAllByEvaluationId(evaluationId).map { it.applicantId }
        } else {
            extractMailSendingTargetsByEvaluationStatus(evaluationId, evaluationStatus).map { it.applicantId }
        }
        return applicantRepository.findAllById(targetApplicantIds)
            .map { MailSendingTargetResponse(it.email) }
    }

    private fun extractMailSendingTargetsByEvaluationStatus(
        evaluationId: Long,
        evaluationStatus: EvaluationStatus
    ): List<EvaluationTarget> {
        val evaluationTargets =
            evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(evaluationId, evaluationStatus)

        return if (evaluationStatus == EvaluationStatus.FAIL) {
            evaluationTargets.filter { it.isAllAssignmentFinished() }
        } else {
            evaluationTargets
        }
    }
}
