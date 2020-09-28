package apply.domain.recruitment

import org.springframework.data.jpa.repository.JpaRepository

interface RecruitmentRepository : JpaRepository<Recruitment, Long> {
    fun findAllByIsHiddenFalse(): List<Recruitment>
}
