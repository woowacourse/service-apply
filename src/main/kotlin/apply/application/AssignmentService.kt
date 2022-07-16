package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getById
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AssignmentService(
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository
) {
    fun create(missionId: Long, userId: Long, request: AssignmentRequest): Long {
        check(!assignmentRepository.existsByUserIdAndMissionId(userId, missionId)) { "이미 제출한 과제 제출물이 존재합니다." }
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting) { "제출 불가능한 과제입니다." }
        findEvaluationTargetOf(mission.evaluationId, userId).passIfBeforeEvaluation()
        val assignment = assignmentRepository.save(
            Assignment(userId, missionId, request.githubUsername, request.pullRequestUrl, request.note)
        )
        return assignment.id
    }

    fun update(missionId: Long, userId: Long, request: AssignmentRequest) {
        check(missionRepository.getById(missionId).isSubmitting) { "제출 불가능한 과제입니다." }
        val assignment = assignmentRepository.findByUserIdAndMissionId(userId, missionId)
            ?: throw IllegalArgumentException("제출한 과제 제출물이 존재하지 않습니다.")
        assignment.update(request.githubUsername, request.pullRequestUrl, request.note)
    }

    private fun findEvaluationTargetOf(evaluationId: Long, userId: Long): EvaluationTarget {
        return evaluationTargetRepository.findByEvaluationIdAndUserId(evaluationId, userId)
            ?: throw IllegalArgumentException("평가 대상자가 아닙니다.")
    }

    fun findByEvaluationTargetId(evaluationTargetId: Long): AssignmentData? {
        val evaluationTarget = evaluationTargetRepository.getById(evaluationTargetId)
        val mission = missionRepository.findByEvaluationId(evaluationTarget.evaluationId) ?: return null
        val assignment = assignmentRepository.findByUserIdAndMissionId(evaluationTarget.userId, mission.id)
        return AssignmentData(assignment)
    }

    fun getByUserIdAndMissionId(userId: Long, missionId: Long): AssignmentResponse {
        val assignment = assignmentRepository.findByUserIdAndMissionId(userId, missionId)
            ?: throw IllegalArgumentException("제출한 과제 제출물이 존재하지 않습니다.")
        return AssignmentResponse(assignment.githubUsername, assignment.pullRequestUrl, assignment.note)
    }
}
