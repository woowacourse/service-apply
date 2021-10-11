package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository

fun AssignmentRepository.getByUserIdAndMissionId(userId: Long, missionId: Long): Assignment {
    return findByUserIdAndMissionId(userId, missionId) ?: throw NoSuchElementException()
}

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByUserIdAndMissionId(userId: Long, missionId: Long): Boolean
    fun findByUserIdAndMissionId(userId: Long, missionId: Long): Assignment?
}
