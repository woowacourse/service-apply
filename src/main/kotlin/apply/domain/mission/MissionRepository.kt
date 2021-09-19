package apply.domain.mission

import org.springframework.data.jpa.repository.JpaRepository

interface MissionRepository : JpaRepository<Mission, Long>
