package apply.domain.applicationform

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationFormRepository : JpaRepository<ApplicationForm, Long> {
    fun findByRecruitmentId(recruitmentId: Long): List<ApplicationForm>

    fun findByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm?

    fun findAllByApplicantId(applicantId: Long): List<ApplicationForm>

    fun existsByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): Boolean
}
