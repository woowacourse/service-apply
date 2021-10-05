package apply.application

import apply.domain.user.UserRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailTargetService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val userRepository: UserRepository
) {
    fun findMailTargets(evaluationId: Long, evaluationStatus: EvaluationStatus? = null): List<MailTargetResponse> {
        val userIds = findEvaluationTargets(evaluationId, evaluationStatus).map { it.userId }
        return userRepository.findAllById(userIds)
            .map { MailTargetResponse(it.name, it.email) }
    }

    private fun findEvaluationTargets(evaluationId: Long, evaluationStatus: EvaluationStatus?): List<EvaluationTarget> {
        return if (evaluationStatus == null) {
            evaluationTargetRepository.findAllByEvaluationId(evaluationId)
        } else {
            findEvaluationTargetsByEvaluationStatus(evaluationId, evaluationStatus)
        }
    }

    private fun findEvaluationTargetsByEvaluationStatus(
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
