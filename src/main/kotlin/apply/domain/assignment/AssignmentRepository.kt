package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByMissionIdAndApplicantId(missionId: Long, applicantId: Long): Boolean
}
