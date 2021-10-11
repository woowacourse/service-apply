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
    fun create(missionId: Long, userId: Long, request: CreateAssignmentRequest) {
        check(!assignmentRepository.existsByUserIdAndMissionId(userId, missionId)) { "이미 제출한 과제물이 존재합니다." }
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting) { "제출 불가능한 과제입니다." }
        findEvaluationTargetOf(mission.evaluationId, userId).passIfBeforeEvaluation()
        assignmentRepository.save(
            Assignment(userId, missionId, request.githubUsername, request.pullRequestUrl, request.note)
        )
    }

    fun findByMissionIdAndEvaluationTargetId(evaluationTargetId: Long, missionId: Long): AssignmentData {
        val evaluationTarget = evaluationTargetRepository.getById(evaluationTargetId) // userId 뽑자
        val assignment = assignmentRepository.findByUserId(evaluationTarget.userId)
        return AssignmentData(assignment)
    }

    private fun findEvaluationTargetOf(evaluationId: Long, userId: Long): EvaluationTarget {
        return evaluationTargetRepository.findByEvaluationIdAndUserId(evaluationId, userId)
            ?: throw IllegalArgumentException("평가 대상자가 아닙니다.")
    }
}
