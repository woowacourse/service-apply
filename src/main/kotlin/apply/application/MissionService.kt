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
