package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByMissionIdAndUserId(missionId: Long, userId: Long): Boolean
}
