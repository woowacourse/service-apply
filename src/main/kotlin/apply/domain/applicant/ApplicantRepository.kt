package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

fun ApplicantRepository.findByEmailAndRecruitmentId(email: String, recruitmentId: Long): Applicant? = findByUserInformationEmailAndRecruitmentId(email, recruitmentId)
fun ApplicantRepository.findAllByEmailIn(emails: List<String>): List<Applicant> = findAllByUserInformationEmailIn(emails)
fun ApplicantRepository.findAllByEmail(email: String): List<Applicant> = findAllByUserInformationEmail(email)
fun ApplicantRepository.existsByEmail(email: String): Boolean = existsByUserInformationEmail(email)

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    @Query("select a from Applicant a where a.user.information.name like %:keyword% or a.user.information.email like %:keyword%")
    fun findAllByKeyword(@Param("keyword") keyword: String): List<Applicant>
    fun findByUserInformationEmailAndRecruitmentId(email: String, recruitmentId: Long): Applicant?
    fun findAllByUserInformationEmailIn(emails: List<String>): List<Applicant>
    fun findAllByUserInformationEmail(email: String): List<Applicant>
    fun existsByUserInformationEmail(email: String): Boolean
}
