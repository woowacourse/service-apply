package apply.domain.user

import org.springframework.data.jpa.repository.JpaRepository

fun UserRepository.findByEmail(email: String): User? = findByInformationEmail(email)
fun UserRepository.existsByEmail(email: String): Boolean = existsByInformationEmail(email)

interface UserRepository : JpaRepository<User, Long> {
    fun findByInformationEmail(email: String): User?
    fun existsByInformationEmail(email: String): Boolean
}
