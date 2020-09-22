package apply.domain.applicationForm

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationFormRepository : JpaRepository<ApplicationForm, Long> {
    fun findByApplicantIdAndRecruitmentId(applicantId: Long, recruitmentId: Long) : ApplicationForm?
}