package apply.domain.administrator

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.NoSuchElementException

fun AdministratorRepository.getByUsername(username: String) = findByUsername(username)
    ?: throw UsernameNotFoundException("관리자를 찾을 수 없습니다.")

fun AdministratorRepository.getById(id: Long) = findByIdOrNull(id)
    ?: throw NoSuchElementException("관리자를 찾을 수 없습니다.")

interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByUsername(username: String): Administrator?
    fun existsByName(name: String): Boolean
    fun existsByUsername(username: String): Boolean
}
