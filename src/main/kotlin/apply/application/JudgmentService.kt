package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getById
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
    fun judgeExample(assignmentId: Long): LastJudgmentResponse {
        val assignment = assignmentRepository.getById(assignmentId)
        val mission = missionRepository.getById(assignment.missionId)
        check(mission.isSubmitting && mission.ableToJudge) { "예제 테스트를 실행할 수 없습니다." }
        val commit = assignmentArchive.getLastCommit(assignment.pullRequestUrl, mission.period.endDateTime)
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignmentId, JudgmentType.EXAMPLE)
            ?: judgmentRepository.save(Judgment(assignmentId, JudgmentType.EXAMPLE))
        judgment.start(commit)
        judgmentRepository.save(judgment)
        return LastJudgmentResponse(judgment.lastStatus, assignment.pullRequestUrl, commit, judgment.getResult(commit))
    }
}
