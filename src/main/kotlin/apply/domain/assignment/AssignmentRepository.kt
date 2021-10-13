package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByUserIdAndMissionId(userId: Long, missionId: Long): Boolean
    fun findByUserIdAndMissionId(userId: Long, missionId: Long): Assignment?
    fun findAllByUserId(userId: Long): List<Assignment>
}
