package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getById
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.mission.JudgmentItemRepository
import apply.domain.mission.getByMissionId
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class JudgmentRequestService(
    private val judgmentItemRepository: JudgmentItemRepository,
    private val judgmentAgency: JudgmentAgency,
    private val assignmentRepository: AssignmentRepository
) {
    @Async
    @TransactionalEventListener
    fun request(event: JudgmentStartedEvent) {
        val assignment = assignmentRepository.getById(event.assignmentId)
        val judgmentItem = judgmentItemRepository.getByMissionId(assignment.missionId)
        judgmentAgency.requestJudge(
            JudgmentRequest(
                event.judgmentId,
                event.type,
                judgmentItem.programmingLanguage,
                judgmentItem.testName,
                assignment.pullRequestUrl,
                event.commit
            )
        )
    }
}
