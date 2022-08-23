package apply.domain.administrator

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException

fun AdministratorRepository.getByUsername(username: String) = findByUsername(username)
    ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")

interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByUsername(username: String): Administrator?
    fun existsByName(name: String): Boolean
    fun existsByUsername(username: String): Boolean
}
