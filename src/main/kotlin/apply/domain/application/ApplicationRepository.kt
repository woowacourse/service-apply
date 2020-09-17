package apply.domain.application

import org.springframework.data.jpa.repository.JpaRepository

interface ApplicationRepository : JpaRepository<Application, Long> {
    fun findByRecruitmentId(recruitmentId: Long): List<Application>

    fun findByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): Application?
}
