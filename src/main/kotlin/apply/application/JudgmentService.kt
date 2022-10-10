package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Commit
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.getById
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JudgmentService(
    private val judgmentRepository: JudgmentRepository,
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentArchive: AssignmentArchive
) {
    @Transactional
    fun judgeExample(userId: Long, missionId: Long): LastJudgmentResponse {
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting) { "예제 테스트를 실행할 수 없습니다." }
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        return judge(mission, assignment, JudgmentType.EXAMPLE)
    }

    @Transactional
    fun judgeReal(userId: Long, missionId: Long): LastJudgmentResponse {
        val mission = missionRepository.getById(missionId)
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        return judge(mission, assignment, JudgmentType.REAL)
    }

    fun judgeAll(missionId: Long) {
        val mission = missionRepository.getById(missionId)
        val assignments = assignmentRepository.findAllByMissionId(missionId)
        for (assignment in assignments) {
            runCatching { judge(mission, assignment, JudgmentType.REAL) }
        }
    }

    private fun judge(mission: Mission, assignment: Assignment, judgmentType: JudgmentType): LastJudgmentResponse {
        check(judgmentItemRepository.existsByMissionId(mission.id)) { "예제 테스트를 실행할 수 없습니다." }
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, judgmentType)
            ?: judgmentRepository.save(Judgment(assignment.id, judgmentType))
        val commit = assignmentArchive.getLastCommit(assignment.pullRequestUrl, mission.period.endDateTime)
        judgment.start(commit)
        judgmentRepository.save(judgment)
        return LastJudgmentResponse(assignment.pullRequestUrl, judgment.lastRecord)
    }

    fun findExample(userId: Long, missionId: Long): LastJudgmentResponse? {
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, JudgmentType.EXAMPLE)
        return judgment?.let { LastJudgmentResponse(assignment.pullRequestUrl, judgment.lastRecord) }
    }

    @Transactional
    fun success(judgmentId: Long, request: SuccessJudgmentRequest) {
        val judgment = judgmentRepository.getById(judgmentId)
        judgment.success(Commit(request.commit), request.passCount, request.totalCount)
        // TODO: reflect result to evaluation answer
    }

    @Transactional
    fun fail(judgmentId: Long, request: FailJudgmentRequest) {
        val judgment = judgmentRepository.getById(judgmentId)
        judgment.fail(Commit(request.commit), request.message)
    }

    @Transactional
    fun cancel(judgmentId: Long, request: CancelJudgmentRequest) {
        val judgment = judgmentRepository.getById(judgmentId)
        judgment.cancel(Commit(request.commit), request.message)
    }
}
