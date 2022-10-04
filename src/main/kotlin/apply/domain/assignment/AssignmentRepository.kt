package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun AssignmentRepository.getByUserIdAndMissionId(userId: Long, missionId: Long): Assignment {
    return findByUserIdAndMissionId(userId, missionId)
        ?: throw NoSuchElementException("과제 제출물이 존재하지 않습니다. userId: $userId, missionId: $missionId")
}

fun AssignmentRepository.getById(id: Long): Assignment = findByIdOrNull(id)
    ?: throw NoSuchElementException("과제 제출물이 존재하지 않습니다. id: $id")

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByUserIdAndMissionId(userId: Long, missionId: Long): Boolean
    fun findByUserIdAndMissionId(userId: Long, missionId: Long): Assignment?
    fun findAllByUserId(userId: Long): List<Assignment>
    fun findAllByMissionId(missionId: Long): List<Assignment>
}
