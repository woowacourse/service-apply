package apply.application

import apply.domain.administrator.AdministratorRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AdministratorService(private val administratorRepository: AdministratorRepository) : UserDetailsService {
    override fun loadUserByUsername(name: String): UserDetails {
        return administratorRepository.findByName(name) ?: throw UsernameNotFoundException("사용자를 찾을 수 없습니다.")
    }
}
