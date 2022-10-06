package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getById
import apply.domain.judgment.JudgmentAgency
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.judgment.getById
import apply.domain.mission.MissionRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class JudgmentRequestService(
    val missionRepository: MissionRepository,
    val judgmentRepository: JudgmentRepository,
    val judgmentAgency: JudgmentAgency,
    val assignmentRepository: AssignmentRepository
) {
    @Async
    @TransactionalEventListener
    fun request(event: JudgmentStartedEvent) {
        val judgment = judgmentRepository.getById(event.judgmentId)
        val assignment = assignmentRepository.getById(judgment.assignmentId)
        val judgmentItem = event.judgmentItem

        judgmentAgency.requestJudge(
            JudgmentRequest(
                event.judgmentId,
                judgmentItem.testName,
                judgmentItem.programmingLanguage,
                judgment.type,
                assignment.pullRequestUrl,
                judgment.lastCommit.hash
            )
        )
    }
}
