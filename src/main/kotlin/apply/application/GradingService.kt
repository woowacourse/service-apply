package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getOrThrow
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getByEvaluationIdAndMemberId
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.judgment.JudgmentSucceededEvent
import apply.domain.judgment.JudgmentTouchedEvent
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.judgmentitem.getByMissionId
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Transactional
@Service
class GradingService(
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(condition = "#event.type.evaluable")
    fun grade(event: JudgmentStartedEvent) {
        grade(event.assignmentId, 0)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(condition = "#event.type.evaluable")
    fun grade(event: JudgmentTouchedEvent) {
        grade(event.assignmentId, event.passCount)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(condition = "#event.type.evaluable")
    fun grade(event: JudgmentSucceededEvent) {
        grade(event.assignmentId, event.passCount)
    }

    private fun grade(assignmentId: Long, score: Int) {
        val assignment = assignmentRepository.getOrThrow(assignmentId)
        val mission = missionRepository.getOrThrow(assignment.missionId)
        val judgmentItem = judgmentItemRepository.getByMissionId(mission.id)
        val target = evaluationTargetRepository.getByEvaluationIdAndMemberId(mission.evaluationId, assignment.memberId)
        target.updateScore(judgmentItem.evaluationItemId, score)
    }
}
