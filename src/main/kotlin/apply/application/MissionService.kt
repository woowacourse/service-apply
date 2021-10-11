package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MissionService(
    private val missionRepository: MissionRepository,
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val assignmentRepository: AssignmentRepository
) {
    fun save(request: MissionData) {
        check(evaluationRepository.existsById(request.evaluation.id)) { "존재하지 않는 평가 id 입니다." }
        check(!missionRepository.existsByEvaluationId(request.evaluation.id)) { "해당 평가에 이미 등록된 과제가 있습니다." }
        missionRepository.save(
            Mission(
                request.title,
                request.description,
                request.evaluation.id,
                request.startDateTime,
                request.endDateTime,
                request.submittable
            )
        )
    }

    fun findAllByRecruitmentId(recruitmentId: Long): List<MissionAndEvaluationResponse> {
        val evaluationsById = evaluationRepository.findAllByRecruitmentId(recruitmentId).associateBy { it.id }
        val missions = missionRepository.findAllByEvaluationIdIn(evaluationsById.keys)
        return missions.map { MissionAndEvaluationResponse(it, evaluationsById.getValue(it.evaluationId)) }
    }

    fun deleteById(id: Long) {
        val mission = missionRepository.getById(id)
        check(!mission.submittable) { "제출 가능한 과제는 삭제할 수 없습니다." }
        missionRepository.deleteById(id)
    }

    fun findAllByUserIdAndRecruitmentId(userId: Long, recruitmentId: Long): List<MissionResponse> {
        val evaluationIds = evaluationRepository.findAllByRecruitmentId(recruitmentId).map { it.id }
        val filteredEvaluationIds = evaluationIds.filter {
            evaluationTargetRepository.existsByUserIdAndEvaluationId(userId, it)
        }
        return missionRepository.findAllByEvaluationIdIn(filteredEvaluationIds).map {
            MissionResponse(
                it.id,
                it.title,
                it.description,
                it.submittable,
                assignmentRepository.existsByUserIdAndMissionId(userId, it.id),
                it.period.startDateTime,
                it.period.endDateTime,
                it.status
            )
        }
    }
}
