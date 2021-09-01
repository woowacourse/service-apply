package apply.domain.cheater

import org.springframework.data.jpa.repository.JpaRepository

interface CheaterRepository : JpaRepository<Cheater, Long> {
    fun existsByEmail(email: String): Boolean
}
