package apply.application

import apply.domain.administrator.Administrator
import apply.domain.administrator.AdministratorRepository
import apply.domain.administrator.getByUsername
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AdministratorService(private val administratorRepository: AdministratorRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return administratorRepository.getByUsername(username)
    }

    fun save(request: AdministratorData): AdministratorResponse {
        check(!administratorRepository.existsByName(request.name)) { "이미 등록된 이름입니다." }
        check(!administratorRepository.existsByUsername(request.username)) { "이미 등록된 아이디입니다." }
        check(request.password == request.passwordConfirmation) { "패스워드가 일치하지 않습니다." }

        return administratorRepository.save(
            Administrator(
                request.name,
                request.username,
                request.password
            )
        ).let(::AdministratorResponse)
    }

    fun findAll() {
        
    }
}
