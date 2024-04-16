package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun AssignmentRepository.getByMemberIdAndMissionId(memberId: Long, missionId: Long): Assignment {
    return findByMemberIdAndMissionId(memberId, missionId)
        ?: throw NoSuchElementException("과제 제출물이 존재하지 않습니다. memberId: $memberId, missionId: $missionId")
}

fun AssignmentRepository.getOrThrow(id: Long): Assignment = findByIdOrNull(id)
    ?: throw NoSuchElementException("과제 제출물이 존재하지 않습니다. id: $id")

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByMemberIdAndMissionId(memberId: Long, missionId: Long): Boolean
    fun findByMemberIdAndMissionId(memberId: Long, missionId: Long): Assignment?
    fun findAllByMemberId(memberId: Long): List<Assignment>
    fun findAllByMissionId(missionId: Long): List<Assignment>
}
