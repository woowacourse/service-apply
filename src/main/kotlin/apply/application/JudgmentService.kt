package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentType
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class JudgmentService(
    private val judgmentRepository: JudgmentRepository,
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val assignmentArchive: AssignmentArchive
) {
    fun judgeExample(userId: Long, missionId: Long): LastJudgmentResponse {
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        val mission = missionRepository.getById(assignment.missionId)
        check(mission.isSubmitting && mission.ableToJudge) { "예제 테스트를 실행할 수 없습니다." }
        val commit = assignmentArchive.getLastCommit(assignment.pullRequestUrl, mission.period.endDateTime)
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, JudgmentType.EXAMPLE)
            ?: judgmentRepository.save(Judgment(assignment.id, JudgmentType.EXAMPLE))
        judgment.start(commit)
        judgmentRepository.save(judgment)
        return LastJudgmentResponse(assignment.pullRequestUrl, judgment.lastRecord)
    }
}
