package apply.domain.applicationform

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationFormRepository : JpaRepository<ApplicationForm, Long> {
    fun findByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicationForm>
    fun findByRecruitmentIdAndUserId(recruitmentId: Long, userId: Long): ApplicationForm?
    fun findAllByUserId(userId: Long): List<ApplicationForm>
    fun findAllByUserIdAndSubmittedTrue(userId: Long): List<ApplicationForm>
    fun existsByRecruitmentIdAndUserId(recruitmentId: Long, userId: Long): Boolean
}
