package apply.application

import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MissionService(
    private val missionRepository: MissionRepository
) {
    fun save(request: MissionData) {
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
}
