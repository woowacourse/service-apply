package apply.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

fun UserRepository.existsByEmail(email: String): Boolean = existsByInformationEmail(email)

interface UserRepository : JpaRepository<User, Long> {
    @Query("select a from User a where a.information.email = :email")
    fun findByEmail(email: String): User?
    fun existsByInformationEmail(email: String): Boolean
}
