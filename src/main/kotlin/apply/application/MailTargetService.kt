package apply.application

import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.user.MemberRepository
import apply.domain.user.findAllByEmailIn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailTargetService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val userRepository: MemberRepository
) {
    fun findMailTargets(evaluationId: Long, evaluationStatus: EvaluationStatus? = null): List<MailTargetResponse> {
        val userIds = findEvaluationTargets(evaluationId, evaluationStatus).map { it.userId }
        return userRepository.findAllById(userIds)
            .map { MailTargetResponse(it.email, it.name) }
    }

    fun findAllByEmails(emails: List<String>): List<MailTargetResponse> {
        val users = userRepository.findAllByEmailIn(emails)
        val anonymousEmails = emails - users.map { it.email }
        return users.map { MailTargetResponse(it) } + anonymousEmails.map { MailTargetResponse(it) }
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
        val evaluationTargets = evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(
            evaluationId, evaluationStatus
        )
        return if (evaluationStatus == EvaluationStatus.FAIL) {
            evaluationTargets.filter { it.evaluated() }
        } else {
            evaluationTargets
        }
    }
}
