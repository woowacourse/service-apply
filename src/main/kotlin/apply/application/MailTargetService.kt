package apply.application

import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.member.MemberRepository
import apply.domain.member.findAllByEmailIn
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
        return memberRepository.findAllById(memberIds)
            .map { MailTargetResponse(it) }
    }

    fun findAllByEmails(emails: List<String>): List<MailTargetResponse> {
        val members = memberRepository.findAllByEmailIn(emails)
        val anonymousEmails = emails - members.map { it.email }
        return members.map { MailTargetResponse(it) } + anonymousEmails.map { MailTargetResponse(it) }
    }

    fun findAllByMemberIds(memberIds: List<Long>): List<MailTargetResponse> {
        // TODO: memberIds에 해당하는 회원이 없는 경우에 대한 처리
        val members = memberRepository.findAllById(memberIds)
        return members.map { MailTargetResponse(it) }
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
