package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    fun findByName(name: String): Applicant?
}
