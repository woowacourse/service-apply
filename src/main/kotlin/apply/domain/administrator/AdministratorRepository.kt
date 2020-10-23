package apply.domain.administrator

import org.springframework.data.jpa.repository.JpaRepository

interface AdministratorRepository : JpaRepository<Administrator, Long> {
    fun findByName(name: String): Administrator?
}
