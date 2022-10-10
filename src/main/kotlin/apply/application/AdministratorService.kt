package apply.application

import apply.domain.administrator.Administrator
import apply.domain.administrator.AdministratorRepository
import apply.domain.administrator.getById
import apply.domain.administrator.getByUsername
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AdministratorService(
    private val administratorRepository: AdministratorRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return administratorRepository.getByUsername(username)
    }

    fun save(request: AdministratorData): AdministratorResponse {
        check(request.password == request.confirmPassword) { "비밀번호가 일치하지 않습니다." }
        val administrator = if (request.id == 0L) create(request) else update(request)
        return administrator.let(::AdministratorResponse)
    }

    private fun create(request: AdministratorData): Administrator {
        check(!administratorRepository.existsByUsername(request.username)) { "이미 등록된 사용자명입니다." }
        return administratorRepository.save(
            Administrator(
                username = request.username,
                name = request.name,
                password = passwordEncoder.encode(request.password)
            )
        )
    }

    private fun update(request: AdministratorData): Administrator {
        return administratorRepository.getById(request.id).apply {
            update(request.name, passwordEncoder.encode(request.password))
        }
    }

    fun findAll(): List<AdministratorResponse> {
        return administratorRepository.findAll().map(::AdministratorResponse)
    }

    fun findById(id: Long): AdministratorResponse {
        return administratorRepository.getById(id).let(::AdministratorResponse)
    }

    fun deleteById(administratorId: Long) {
        administratorRepository.deleteById(administratorId)
    }
}
