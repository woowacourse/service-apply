package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getByEvaluationId
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class JudgmentAllService(
    private val judgmentService: JudgmentService,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository
) {
    @Async
    fun judgeAll(evaluationId: Long) {
        val mission = missionRepository.getByEvaluationId(evaluationId)
        check(judgmentItemRepository.existsByMissionId(mission.id)) { "자동 채점을 실행할 수 없습니다." }
        val assignments = assignmentRepository.findAllByMissionId(mission.id)
        assignments.forEach {
            runCatching { judgmentService.judge(mission, it, JudgmentType.REAL) }
        }
    }
}
