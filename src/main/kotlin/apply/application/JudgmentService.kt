package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getById
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getByEvaluationIdAndUserId
import apply.domain.evaluationtarget.getById
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.Commit
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentSucceededEvent
import apply.domain.judgment.JudgmentTouchedEvent
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.getById
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.judgmentitem.getByMissionId
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Transactional
@Service
class JudgmentService(
    private val judgmentRepository: JudgmentRepository,
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val assignmentArchive: AssignmentArchive
) {
    fun judgeExample(userId: Long, missionId: Long): LastJudgmentResponse {
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting && judgmentItemRepository.existsByMissionId(mission.id)) {
            "예제 테스트를 실행할 수 없습니다."
        }
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        return judge(mission, assignment, JudgmentType.EXAMPLE)
    }

    fun findExample(userId: Long, missionId: Long): LastJudgmentResponse? {
        val assignment = assignmentRepository.findByUserIdAndMissionId(userId, missionId) ?: return null
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, JudgmentType.EXAMPLE)
        return judgment?.let { LastJudgmentResponse(assignment.pullRequestUrl, it.lastRecord) }
    }

    fun success(judgmentId: Long, request: SuccessJudgmentRequest) {
        val judgment = judgmentRepository.getById(judgmentId)
        judgment.success(Commit(request.commit), request.passCount, request.totalCount)
        judgmentRepository.save(judgment)
    }

    @TransactionalEventListener(condition = "#event.type.name() == 'REAL'")
    fun changeAnswer(event: JudgmentTouchedEvent) {
        val assignment = assignmentRepository.getById(event.assignmentId)
        val mission = missionRepository.getById(assignment.missionId)
        val judgmentItem = judgmentItemRepository.getByMissionId(mission.id)
        val evaluationTarget = evaluationTargetRepository
            .getByEvaluationIdAndUserId(mission.evaluationId, assignment.userId)
        evaluationTarget.addEvaluationAnswer(EvaluationAnswer(event.passCount, judgmentItem.evaluationItemId))
    }

    @TransactionalEventListener(condition = "#event.type.name() == 'REAL'")
    fun changeAnswer(event: JudgmentSucceededEvent) {
        val assignment = assignmentRepository.getById(event.assignmentId)
        val mission = missionRepository.getById(assignment.missionId)
        val judgmentItem = judgmentItemRepository.getByMissionId(mission.id)
        val evaluationTarget = evaluationTargetRepository
            .getByEvaluationIdAndUserId(mission.evaluationId, assignment.userId)
        evaluationTarget.addEvaluationAnswer(EvaluationAnswer(event.passCount, judgmentItem.evaluationItemId))
    }

    fun fail(judgmentId: Long, request: FailJudgmentRequest) {
        val judgment = judgmentRepository.getById(judgmentId)
        judgment.fail(Commit(request.commit), request.message)
        judgmentRepository.save(judgment)
    }

    fun cancel(judgmentId: Long, request: CancelJudgmentRequest) {
        val judgment = judgmentRepository.getById(judgmentId)
        judgment.cancel(Commit(request.commit), request.message)
        judgmentRepository.save(judgment)
    }

    fun judgeReal(assignmentId: Long): LastJudgmentResponse {
        val assignment = assignmentRepository.getById(assignmentId)
        val mission = missionRepository.getById(assignment.missionId)
        check(judgmentItemRepository.existsByMissionId(mission.id)) { "자동 채점을 실행할 수 없습니다." }
        return judge(mission, assignment, JudgmentType.REAL)
    }

    fun judge(mission: Mission, assignment: Assignment, judgmentType: JudgmentType): LastJudgmentResponse {
        var judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, judgmentType)
            ?: judgmentRepository.save(Judgment(assignment.id, judgmentType))
        val commit = assignmentArchive.getLastCommit(assignment.pullRequestUrl, mission.period.endDateTime)
        judgment.start(commit)
        judgment = judgmentRepository.save(judgment)
        return LastJudgmentResponse(assignment.pullRequestUrl, judgment.lastRecord)
    }

    fun findByEvaluationTargetId(evaluationTargetId: Long, type: JudgmentType): JudgmentData? {
        val evaluationTarget = evaluationTargetRepository.getById(evaluationTargetId)
        val mission = missionRepository.findByEvaluationId(evaluationTarget.evaluationId) ?: return null
        val judgmentItem = judgmentItemRepository.findByMissionId(mission.id) ?: return null
        val assignment = assignmentRepository.findByUserIdAndMissionId(evaluationTarget.userId, mission.id)
        return assignment
            ?.let { judgmentRepository.findByAssignmentIdAndType(it.id, type) }
            .let {
                JudgmentData(
                    id = it?.id,
                    evaluationItemId = judgmentItem.evaluationItemId,
                    assignmentId = assignment?.id,
                    judgmentRecord = it?.lastRecord
                )
            }
    }
}
