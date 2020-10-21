package apply.application

import apply.domain.administrator.Administrator
import apply.domain.administrator.AdministratorRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class AdministratorService(private val administratorRepository: AdministratorRepository) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        return administratorRepository.findByName(name)
    }

    @PostConstruct
    private fun populateDummy() {
        if (administratorRepository.count() != 0L) {
            return
        }
        val administrator = Administrator(
            id = 1L,
            name = "admin",
            password = "{noop}1234"
        )
        administratorRepository.save(administrator)
    }
}
