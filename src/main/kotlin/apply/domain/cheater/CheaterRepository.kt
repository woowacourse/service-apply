package apply.domain.cheater

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun CheaterRepository.getById(id: Long): Cheater = findByIdOrNull(id)
    ?: throw NoSuchElementException("부정행위자가 존재하지 않습니다. id: $id")

interface CheaterRepository : JpaRepository<Cheater, Long> {
    fun existsByEmail(email: String): Boolean
}
