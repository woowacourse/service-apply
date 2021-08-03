package apply.domain.recruitment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun RecruitmentRepository.getById(recruitmentId: Long): Recruitment {
    return findByIdOrNull(recruitmentId) ?: throw NoSuchElementException()
}

interface RecruitmentRepository : JpaRepository<Recruitment, Long> {
    fun findAllByHiddenFalse(): List<Recruitment>
    fun findAllByTermId(termId: Long): List<Recruitment>
}
