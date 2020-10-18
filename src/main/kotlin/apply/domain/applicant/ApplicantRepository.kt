package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    fun findByInformationNameContainingOrInformationEmailContaining(name: String, email: String): List<Applicant>

    @Query("SELECT a FROM Applicant a WHERE a.information.email = :email")
    fun findByEmail(@Param("email") email: String): Applicant?
}
