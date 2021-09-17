package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

fun ApplicantRepository.findByEmail(email: String): Applicant? = findByInformationEmail(email)
fun ApplicantRepository.findAllByEmailIn(emails: List<String>): List<Applicant> = findAllByInformationEmailIn(emails)
fun ApplicantRepository.existsByEmail(email: String): Boolean = existsByInformationEmail(email)

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    @Query("select a from Applicant a where a.information.name like %:keyword% or a.information.email like %:keyword%")
    fun findAllByKeyword(@Param("keyword") keyword: String): List<Applicant>
    fun findByInformationEmail(@Param("email") email: String): Applicant?
    fun findAllByInformationEmailIn(@Param("emails") emails: List<String>): List<Applicant>
    fun existsByInformationEmail(@Param("email") email: String): Boolean
}
