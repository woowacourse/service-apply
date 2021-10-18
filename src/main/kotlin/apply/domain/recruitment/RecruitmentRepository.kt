package apply.domain.recruitment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun RecruitmentRepository.getById(recruitmentId: Long): Recruitment {
    return findByIdOrNull(recruitmentId) ?: throw NoSuchElementException("해당 모집은 존재하지 않습니다.")
}

interface RecruitmentRepository : JpaRepository<Recruitment, Long> {
    fun findAllByHiddenFalse(): List<Recruitment>
    fun findAllByTermId(termId: Long): List<Recruitment>
}
