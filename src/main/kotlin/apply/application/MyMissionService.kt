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
import apply.domain.mission.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.markdownToEmbeddedHtml

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
    fun findAllByMemberIdAndRecruitmentId(memberId: Long, recruitmentId: Long): List<MyMissionAndJudgementResponse> {
        val missions = findMissions(memberId, recruitmentId)
        if (missions.isEmpty()) return emptyList()

        val assignments = assignmentRepository.findAllByMemberId(memberId)
        if (assignments.isEmpty()) return missions.map(::MyMissionAndJudgementResponse)

        val judgmentItems = judgmentItemRepository.findAllByMissionIdIn(missions.map { it.id })
        if (judgmentItems.isEmpty()) return missions.mapBy(assignments)

        val judgments = judgmentRepository
            .findAllByAssignmentIdInAndType(assignments.map { it.id }, JudgmentType.EXAMPLE)
        return missions.mapBy(assignments, judgmentItems, judgments)
    }

    private fun findMissions(memberId: Long, recruitmentId: Long): List<Mission> {
        val evaluationIds = evaluationRepository.findAllByRecruitmentId(recruitmentId).map { it.id }
        val targets = evaluationTargetRepository.findAllByMemberIdAndEvaluationIdIn(memberId, evaluationIds)
        return missionRepository.findAllByEvaluationIdIn(targets.map { it.id }).filterNot { it.hidden }
    }

    private fun List<Mission>.mapBy(assignments: List<Assignment>): List<MyMissionAndJudgementResponse> {
        return map { mission ->
            val assignment = assignments.find { it.missionId == mission.id }
            MyMissionAndJudgementResponse(mission, assignment != null)
        }
    }

    private fun List<Mission>.mapBy(
        assignments: List<Assignment>,
        judgmentItems: List<JudgmentItem>,
        judgments: List<Judgment>
    ): List<MyMissionAndJudgementResponse> {
        return map { mission ->
            val assignment = assignments.find { it.missionId == mission.id }
            val judgmentItem = judgmentItems.find { it.missionId == mission.id }
            val judgment = judgments.findLastJudgment(assignment, judgmentItem)
            MyMissionAndJudgementResponse(
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
        return LastJudgmentResponse(assignment.pullRequestUrl, judgment.lastRecord)
    }

    fun findLastRealJudgmentByEvaluationTargetId(evaluationTargetId: Long): JudgmentData? {
        val evaluationTarget = evaluationTargetRepository.getOrThrow(evaluationTargetId)
        val mission = missionRepository.findByEvaluationId(evaluationTarget.evaluationId) ?: return null
        val judgmentItem = judgmentItemRepository.findByMissionId(mission.id) ?: return null
        val assignment = assignmentRepository.findByMemberIdAndMissionId(evaluationTarget.memberId, mission.id)
            ?: return JudgmentData(evaluationItemId = judgmentItem.evaluationItemId)
        val judgment = judgmentRepository.findByAssignmentIdAndType(assignment.id, JudgmentType.REAL)
        return JudgmentData(
            id = judgment?.id,
            evaluationItemId = judgmentItem.evaluationItemId,
            assignmentId = assignment.id,
            judgmentRecord = judgment?.lastRecord
        )
    }

    fun findByMemberIdAndMissionId(memberId: Long, missionId: Long): MyMissionResponse {
        val mission = missionRepository.getOrThrow(missionId)
        val evaluationTarget = evaluationTargetRepository.findByEvaluationIdAndMemberId(mission.evaluationId, memberId)
        if (!mission.isDescriptionViewable || evaluationTarget == null) {
            throw NoSuchElementException("과제가 존재하지 않습니다. id: $missionId")
        }
        val assignment = assignmentRepository.findByMemberIdAndMissionId(memberId, missionId)
        return MyMissionResponse(
            mission = mission,
            description = markdownToEmbeddedHtml(mission.description),
            submitted = assignment != null,
        )
    }
}
