package apply.domain.administrator

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException

fun AdministratorRepository.getByUsername(username: String) = findByUsername(username)
    ?: throw UsernameNotFoundException("관리자가 존재하지 않습니다. username: $username")

fun AdministratorRepository.getById(id: Long) = findByIdOrNull(id)
    ?: throw NoSuchElementException("관리자가 존재하지 않습니다. id: $id")

interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByUsername(username: String): Administrator?
    fun existsByUsername(username: String): Boolean
}
