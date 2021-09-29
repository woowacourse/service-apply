package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
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
    fun create(missionId: Long, applicantId: Long, assignmentData: CreateAssignmentRequest) {
        check(!assignmentRepository.existsByMissionIdAndApplicantId(missionId, applicantId)) {
            "이미 제출한 과제물이 존재합니다."
        }

        val mission = missionRepository.getById(missionId)
        check(mission.isProgressing) {
            "제출 불가능한 과제입니다."
        }

        val evaluationTarget = findEvaluationTargetOf(mission.evaluationId, applicantId)
        if (evaluationTarget.isWaiting) {
            evaluationTarget.evaluationStatus = EvaluationStatus.PASS
        }

        assignmentRepository.save(
            Assignment(
                applicantId,
                missionId,
                assignmentData.githubId,
                assignmentData.url,
                assignmentData.impression
            )
        )
    }

    private fun findEvaluationTargetOf(evaluationId: Long, applicantId: Long): EvaluationTarget {
        return evaluationTargetRepository.findByEvaluationIdAndApplicantId(evaluationId, applicantId)
            ?: throw IllegalArgumentException("평가 대상자가 아닙니다.")
    }
}
