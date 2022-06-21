package apply.domain.recruitment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun RecruitmentRepository.getById(id: Long): Recruitment = findByIdOrNull(id)
    ?: throw NoSuchElementException("모집이 존재하지 않습니다. id: $id")

interface RecruitmentRepository : JpaRepository<Recruitment, Long> {
    fun findAllByHiddenFalse(): List<Recruitment>
    fun findAllByTermId(termId: Long): List<Recruitment>
    fun existsByTermId(termId: Long): Boolean
}
