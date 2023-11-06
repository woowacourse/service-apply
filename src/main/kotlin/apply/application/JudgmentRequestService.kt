package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getOrThrow
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.judgmentitem.getByMissionId
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class JudgmentRequestService(
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val judgmentAgency: JudgmentAgency
) {
    @Async
    @TransactionalEventListener
    fun request(event: JudgmentStartedEvent) {
        val assignment = assignmentRepository.getOrThrow(event.assignmentId)
        val mission = missionRepository.getOrThrow(assignment.missionId)
        val judgmentItem = judgmentItemRepository.getByMissionId(assignment.missionId)
        judgmentAgency.requestJudge(
            JudgmentRequest(
                event.judgmentId,
                event.type,
                judgmentItem.programmingLanguage,
                judgmentItem.testName,
                mission.submissionMethod,
                assignment.url,
                event.commit
            )
        )
    }
}
