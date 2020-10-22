package apply.domain.applicationform

import apply.application.ApplicantAndFormResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicationFormRepository : JpaRepository<ApplicationForm, Long> {
    fun findByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicationForm>

    @Query(
        """
        select new apply.application.ApplicantAndFormResponse (a, c, f)
        from Applicant a inner join ApplicationForm f on a.id = f.applicantId 
            left join Cheater c on c.applicantId = a.id 
        where f.recruitmentId = :recruitmentId and f.submitted = true
            and (a.information.name like %:keyword% or a.information.email like %:keyword%)
        """
    )
    fun findByRecruitmentIdAndKeyword(
        recruitmentId: Long,
        keyword: String,
        pageable: Pageable
    ): Page<ApplicantAndFormResponse>

    fun findByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm?

    fun findAllByApplicantId(applicantId: Long): List<ApplicationForm>

    fun existsByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): Boolean

    fun existsByApplicantIdAndSubmittedTrue(applicantId: Long): Boolean
}
