package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    fun findByName(name: String): Optional<Applicant>
}
