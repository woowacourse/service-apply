package apply.application

import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.member.MemberRepository
import apply.domain.member.findAllByEmailIn
import apply.domain.member.findAllByIdIn
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailTargetService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val memberRepository: MemberRepository
) {
    fun findMailTargets(evaluationId: Long, evaluationStatus: EvaluationStatus? = null): List<MailTargetResponse> {
        val memberIds = findEvaluationTargets(evaluationId, evaluationStatus).map { it.memberId }
        return memberRepository.findAllByIdIn(memberIds)
            .map { MailTargetResponse(it.email, it.name) }
    }

    fun findAllByEmails(emails: List<String>): List<MailTargetResponse> {
        val members = memberRepository.findAllByEmailIn(emails)
        val anonymousEmails = emails - members.map { it.email }
        return members.map { MailTargetResponse(it) } + anonymousEmails.map { MailTargetResponse(it) }
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
