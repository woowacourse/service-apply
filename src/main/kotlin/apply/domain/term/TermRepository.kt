package apply.domain.term

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun TermRepository.getById(id: Long): Term {
    if (id == 0L) {
        return Term.SINGLE
    }
    return findByIdOrNull(id) ?: throw NoSuchElementException("해당 내용은 존재하지 않습니다.")
}

interface TermRepository : JpaRepository<Term, Long>
