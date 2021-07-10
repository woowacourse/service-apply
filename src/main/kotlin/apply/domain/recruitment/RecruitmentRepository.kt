package apply.domain.recruitment

import org.springframework.data.jpa.repository.JpaRepository

interface RecruitmentRepository : JpaRepository<Recruitment, Long> {
    fun findAllByHiddenFalse(): List<Recruitment>

    fun findTermById(id: Long): Long?
}
