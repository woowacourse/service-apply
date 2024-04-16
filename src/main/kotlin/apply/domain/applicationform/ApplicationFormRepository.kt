package apply.domain.applicationform

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationFormRepository : JpaRepository<ApplicationForm, Long> {
    fun findByRecruitmentIdAndSubmittedTrue(recruitmentId: Long): List<ApplicationForm>
    fun findByRecruitmentIdAndMemberId(recruitmentId: Long, memberId: Long): ApplicationForm?
    fun findAllByMemberId(memberId: Long): List<ApplicationForm>
    fun findAllByMemberIdAndSubmittedTrue(memberId: Long): List<ApplicationForm>
    fun existsByRecruitmentIdAndMemberId(recruitmentId: Long, memberId: Long): Boolean
}
