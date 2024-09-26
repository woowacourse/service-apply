package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByMemberIdAndMissionId
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getOrThrow
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AssignmentService(
    private val assignmentRepository: AssignmentRepository,
    private val missionRepository: MissionRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository
) {
    fun create(missionId: Long, memberId: Long, request: AssignmentRequest): AssignmentResponse {
        check(!assignmentRepository.existsByMemberIdAndMissionId(memberId, missionId)) { "이미 제출한 과제 제출물이 존재합니다." }
        val mission = missionRepository.getOrThrow(missionId)
        check(mission.isSubmitting) { "제출 불가능한 과제입니다." }
        findEvaluationTargetOf(mission.evaluationId, memberId).passIfBeforeEvaluation()
        return assignmentRepository
            .save(Assignment(memberId, missionId, request.url, request.note))
            .let(::AssignmentResponse)
    }

    fun update(missionId: Long, memberId: Long, request: AssignmentRequest) {
        check(missionRepository.getOrThrow(missionId).isSubmitting) { "제출 불가능한 과제입니다." }
        val assignment = assignmentRepository.getByMemberIdAndMissionId(memberId, missionId)
        assignment.update(request.url, request.note)
    }

    private fun findEvaluationTargetOf(evaluationId: Long, memberId: Long): EvaluationTarget {
        return evaluationTargetRepository.findByEvaluationIdAndMemberId(evaluationId, memberId)
            ?: throw IllegalArgumentException("평가 대상자가 아닙니다.")
    }

    fun getByMemberIdAndMissionId(memberId: Long, missionId: Long): AssignmentResponse {
        val assignment = assignmentRepository.getByMemberIdAndMissionId(memberId, missionId)
        return AssignmentResponse(assignment)
    }

    fun findByEvaluationTargetId(evaluationTargetId: Long): AssignmentData? {
        val evaluationTarget = evaluationTargetRepository.getOrThrow(evaluationTargetId)
        val mission = missionRepository.findByEvaluationId(evaluationTarget.evaluationId) ?: return null
        val assignment = assignmentRepository.findByMemberIdAndMissionId(evaluationTarget.memberId, mission.id)
        return AssignmentData(assignment)
    }
}
