package apply.application

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
}
