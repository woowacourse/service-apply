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

    fun findAllByRecruitmentId(recruitmentId: Long): List<MissionResponse> {
        val evaluations = evaluationRepository.findAllByRecruitmentId(recruitmentId)
            .associateBy { it.id }

        val evaluationIds = evaluations.keys.toList()
        val missions = missionRepository.findAllByEvaluationIdIn(evaluationIds)

        return missions.map { MissionResponse(it, evaluations.getValue(it.evaluationId)) }
    }

    fun deleteById(id: Long) {
        val mission = missionRepository.findByIdOrNull(id) ?: throw IllegalArgumentException("해당 id의 과제를 찾을 수 없습니다.")
        check(mission.isSubmittable) { "현재 제출가능한 과제는 삭제할 수 없습니다." }

        missionRepository.deleteById(id)
    }
}
