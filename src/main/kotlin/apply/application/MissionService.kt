package apply.application

import apply.domain.evaluation.EvaluationRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MissionService(
    private val missionRepository: MissionRepository,
    private val evaluationRepository: EvaluationRepository
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

    fun getById(id: Long): MissionData {
        val mission = missionRepository.getById(id)
        return MissionData(mission)
    }

    fun update(missionId: Long, request: UpdateMissionRequest) {
        val mission = missionRepository.findByIdOrNull(missionId) ?: throw IllegalArgumentException("존재하지 않는 미션입니다.")
        mission.update(request)
    }

    fun findAllByRecruitmentId(recruitmentId: Long): List<MissionResponse> {
        val evaluationsById = evaluationRepository.findAllByRecruitmentId(recruitmentId).associateBy { it.id }
        val missions = missionRepository.findAllByEvaluationIdIn(evaluationsById.keys)
        return missions.map { MissionResponse(it, evaluationsById.getValue(it.evaluationId)) }
    }

    fun deleteById(id: Long) {
        val mission = missionRepository.getById(id)
        check(!mission.submittable) { "제출 가능한 과제는 삭제할 수 없습니다." }
        missionRepository.deleteById(id)
    }
}
