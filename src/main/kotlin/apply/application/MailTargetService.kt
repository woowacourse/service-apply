package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailTargetService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val applicantRepository: ApplicantRepository,
) {
    fun findMailTargets(evaluationId: Long, evaluationStatus: EvaluationStatus? = null): List<MailTargetResponse> {
        val targetApplicantIds = findTargetIds(evaluationStatus, evaluationId)
        return applicantRepository.findAllById(targetApplicantIds)
            .map { MailTargetResponse(it.email) }
    }

    private fun findTargetIds(evaluationStatus: EvaluationStatus?, evaluationId: Long): List<Long> {
        return if (evaluationStatus == null) {
            evaluationTargetRepository.findAllByEvaluationId(evaluationId)
        } else {
            findEvaluationTargets(evaluationId, evaluationStatus)
        }.map { it.applicantId }
    }

    private fun findEvaluationTargets(
        evaluationId: Long,
        evaluationStatus: EvaluationStatus
    ): List<EvaluationTarget> {
        val evaluationTargets =
            evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(evaluationId, evaluationStatus)

        return if (evaluationStatus == EvaluationStatus.FAIL) {
            evaluationTargets.filter { it.submitted() }
        } else {
            evaluationTargets
        }
    }
}
