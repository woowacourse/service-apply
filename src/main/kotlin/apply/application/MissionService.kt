package apply.application

import apply.domain.mission.MissionRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MissionService(
    private val missionRepository: MissionRepository
) {
    fun save(request: MissionData) {
        // TODO: 생성 기능 구현
    }
}
