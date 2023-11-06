package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.assignment.getOrThrow
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Commit
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.getOrThrow
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class JudgmentService(
    private val judgmentRepository: JudgmentRepository,
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentArchive: AssignmentArchive
) {
    fun judgeExample(userId: Long, missionId: Long): LastJudgmentResponse {
        val mission = missionRepository.getOrThrow(missionId)
        check(mission.isSubmitting && judgmentItemRepository.existsByMissionId(mission.id)) {
            "예제 테스트를 실행할 수 없습니다."
        }
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        return judge(mission, assignment, JudgmentType.EXAMPLE)
    }

    fun findLastExampleJudgment(userId: Long, missionId: Long): LastJudgmentResponse? {
        val assignment = assignmentRepository.findByUserIdAndMissionId(userId, missionId) ?: return null
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, JudgmentType.EXAMPLE)
        return judgment?.let { LastJudgmentResponse(assignment.url, it.lastRecord) }
    }

    fun success(judgmentId: Long, request: SuccessJudgmentRequest) {
        val judgment = judgmentRepository.getOrThrow(judgmentId)
        judgment.success(Commit(request.commit), request.passCount, request.totalCount)
        judgmentRepository.save(judgment)
    }

    fun fail(judgmentId: Long, request: FailJudgmentRequest) {
        val judgment = judgmentRepository.getOrThrow(judgmentId)
        judgment.fail(Commit(request.commit), request.message)
        judgmentRepository.save(judgment)
    }

    fun cancel(judgmentId: Long, request: CancelJudgmentRequest) {
        val judgment = judgmentRepository.getOrThrow(judgmentId)
        judgment.cancel(Commit(request.commit), request.message)
        judgmentRepository.save(judgment)
    }

    fun judgeReal(assignmentId: Long): LastJudgmentResponse {
        val assignment = assignmentRepository.getOrThrow(assignmentId)
        val mission = missionRepository.getOrThrow(assignment.missionId)
        check(judgmentItemRepository.existsByMissionId(mission.id)) { "자동 채점을 실행할 수 없습니다." }
        return judge(mission, assignment, JudgmentType.REAL)
    }

    fun judge(mission: Mission, assignment: Assignment, judgmentType: JudgmentType): LastJudgmentResponse {
        val commit = assignmentArchive.getLastCommit(
            mission.submissionMethod, assignment.url, mission.period.endDateTime
        )
        var judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, judgmentType)
            ?: judgmentRepository.save(Judgment(assignment.id, judgmentType))
        judgment.start(commit)
        judgment = judgmentRepository.save(judgment)
        return LastJudgmentResponse(assignment.url, judgment.lastRecord)
    }
}
