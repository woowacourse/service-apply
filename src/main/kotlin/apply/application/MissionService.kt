package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.judgmentitem.JudgmentItem
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MissionService(
    private val missionRepository: MissionRepository,
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgmentItemRepository: JudgmentItemRepository
) {
    fun save(request: MissionData): MissionResponse {
        validate(request)
        val mission = missionRepository.save(
            Mission(
                request.title,
                request.description,
                request.evaluation.id,
                request.startDateTime,
                request.endDateTime,
                request.submittable,
                request.hidden,
                request.id
            )
        )
        judgmentItemRepository.findByMissionId(mission.id)
            ?.update(request)
            ?: createJudgmentItem(request, mission.id)
        return MissionResponse(mission)
    }

    private fun validate(request: MissionData) {
        val evaluationId = request.evaluation.id
        require(evaluationRepository.existsById(evaluationId)) { "평가가 존재하지 않습니다. id: $evaluationId" }
        if (isNew(request)) {
            check(!missionRepository.existsByEvaluationId(evaluationId)) {
                "이미 과제가 등록된 평가입니다. evaluationId: $evaluationId"
            }
        } else {
            val mission = missionRepository.getById(request.id)
            check(mission.evaluationId == evaluationId) {
                "과제의 평가는 수정할 수 없습니다."
            }
        }
    }

    private fun isNew(request: MissionData): Boolean {
        return request.id == 0L || !missionRepository.existsById(request.id)
    }

    private fun JudgmentItem.update(request: MissionData) {
        if (request.judgmentItemData == JudgmentItemData()) {
            judgmentItemRepository.deleteByMissionId(request.id)
        } else {
            update(
                request.judgmentItemData.evaluationItemSelectData.id,
                request.judgmentItemData.testName,
                request.judgmentItemData.programmingLanguage
            )
        }
    }

    private fun createJudgmentItem(request: MissionData, missionId: Long) {
        if (request.judgmentItemData != JudgmentItemData()) {
            judgmentItemRepository.save(
                JudgmentItem(
                    missionId,
                    request.judgmentItemData.evaluationItemSelectData.id,
                    request.judgmentItemData.testName,
                    request.judgmentItemData.programmingLanguage
                )
            )
        }
    }

    fun getById(id: Long): MissionResponse {
        val mission = missionRepository.getById(id)
        return MissionResponse(mission)
    }

    fun findAllByRecruitmentId(recruitmentId: Long): List<MissionAndEvaluationResponse> {
        val evaluationsById = evaluationRepository.findAllByRecruitmentId(recruitmentId).associateBy { it.id }
        val missions = missionRepository.findAllByEvaluationIdIn(evaluationsById.keys)
        return missions.map { MissionAndEvaluationResponse(it, evaluationsById.getValue(it.evaluationId)) }
    }

    fun findAllByUserIdAndRecruitmentId(userId: Long, recruitmentId: Long): List<MyMissionResponse> {
        val evaluationIds = evaluationRepository.findAllByRecruitmentId(recruitmentId).map { it.id }
        val includedEvaluationIds = evaluationIds
            .filter { evaluationTargetRepository.existsByUserIdAndEvaluationId(userId, it) }
        val assignments = assignmentRepository.findAllByUserId(userId)
        return missionRepository.findAllByEvaluationIdIn(includedEvaluationIds)
            .filterNot { it.hidden }
            .map { mission -> MyMissionResponse(mission, assignments.any { it.missionId == mission.id }) }
    }

    fun deleteById(id: Long) {
        val mission = missionRepository.getById(id)
        check(!mission.submittable) { "제출 가능한 과제는 삭제할 수 없습니다." }
        missionRepository.deleteById(id)
    }

    fun getDataById(id: Long): MissionData {
        val mission = missionRepository.getById(id)
        val judgmentItemData = judgmentItemRepository.findByMissionId(mission.id)
            ?.let { JudgmentItemData(it, findEvaluationItemData(it.evaluationItemId)) }
            ?: JudgmentItemData()
        val evaluation = evaluationRepository.getById(mission.evaluationId)
        return MissionData(mission, evaluation, judgmentItemData)
    }

    private fun findEvaluationItemData(evaluationItemId: Long): EvaluationItemSelectData {
        return evaluationItemRepository
            .findByIdOrNull(evaluationItemId)
            ?.let(::EvaluationItemSelectData)
            ?: EvaluationItemSelectData()
    }
}
