package apply.domain.judgment.tobe

import apply.domain.mission.MissionRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener

@Service
class JudgmentRequestService(
    val missionRepository: MissionRepository,
    val judgmentRepository: JudgmentRepository
) {
    @Async
    @TransactionalEventListener
    fun request(event: JudgmentStartedEvent) {
        TODO("Not yet implemented")
    }
}
