package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByMissionIdAndApplicantId(missionId: Long, applicantId: Long): Boolean
}
