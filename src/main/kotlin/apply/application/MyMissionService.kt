package apply.application

import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getOrThrow
import apply.domain.judgment.Judgment
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgmentitem.JudgmentItem
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class MyMissionService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgmentRepository: JudgmentRepository
) {
    fun findAllByUserIdAndRecruitmentId(userId: Long, recruitmentId: Long): List<MyMissionResponse> {
        val missions = findMissions(userId, recruitmentId)
        if (missions.isEmpty()) return emptyList()

        val assignments = assignmentRepository.findAllByUserId(userId)
        if (assignments.isEmpty()) return missions.map(::MyMissionResponse)

        val judgmentItems = judgmentItemRepository.findAllByMissionIdIn(missions.map { it.id })
        if (judgmentItems.isEmpty()) return missions.mapBy(assignments)

        val judgments = judgmentRepository
            .findAllByAssignmentIdInAndType(assignments.map { it.id }, JudgmentType.EXAMPLE)
        return missions.mapBy(assignments, judgmentItems, judgments)
    }

    private fun findMissions(userId: Long, recruitmentId: Long): List<Mission> {
        val evaluationIds = evaluationRepository.findAllByRecruitmentId(recruitmentId).map { it.id }
        val targets = evaluationTargetRepository.findAllByUserIdAndEvaluationIdIn(userId, evaluationIds)
        return missionRepository.findAllByEvaluationIdIn(targets.map { it.id }).filterNot { it.hidden }
    }

    private fun List<Mission>.mapBy(assignments: List<Assignment>): List<MyMissionResponse> {
        return map { mission ->
            val assignment = assignments.find { it.missionId == mission.id }
            MyMissionResponse(mission, assignment != null)
        }
    }

    private fun List<Mission>.mapBy(
        assignments: List<Assignment>,
        judgmentItems: List<JudgmentItem>,
        judgments: List<Judgment>
    ): List<MyMissionResponse> {
        return map { mission ->
            val assignment = assignments.find { it.missionId == mission.id }
            val judgmentItem = judgmentItems.find { it.missionId == mission.id }
            val judgment = judgments.findLastJudgment(assignment, judgmentItem)
            MyMissionResponse(
                mission = mission,
                submitted = assignment != null,
                runnable = assignment != null && judgmentItem != null,
                judgment = judgment
            )
        }
    }

    private fun List<Judgment>.findLastJudgment(
        assignment: Assignment?,
        judgmentItem: JudgmentItem?
    ): LastJudgmentResponse? {
        if (assignment == null || judgmentItem == null) return null
        val judgment = find { it.assignmentId == assignment.id } ?: return null
        return LastJudgmentResponse(assignment.url, judgment.lastRecord)
    }

    fun findLastRealJudgmentByEvaluationTargetId(evaluationTargetId: Long): JudgmentData? {
        val evaluationTarget = evaluationTargetRepository.getOrThrow(evaluationTargetId)
        val mission = missionRepository.findByEvaluationId(evaluationTarget.evaluationId) ?: return null
        val judgmentItem = judgmentItemRepository.findByMissionId(mission.id) ?: return null
        val assignment = assignmentRepository.findByUserIdAndMissionId(evaluationTarget.userId, mission.id)
            ?: return JudgmentData(evaluationItemId = judgmentItem.evaluationItemId)
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, JudgmentType.REAL)
        return JudgmentData(
            id = judgment?.id,
            evaluationItemId = judgmentItem.evaluationItemId,
            assignmentId = assignment.id,
            judgmentRecord = judgment?.lastRecord
        )
    }
}
