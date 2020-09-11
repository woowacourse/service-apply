package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    fun findByNameContainingOrEmailContaining(name: String, email: String): List<Applicant>
}
