package apply.domain.cheater

import apply.domain.applicant.Applicant
import org.springframework.data.jpa.repository.JpaRepository

interface CheaterRepository : JpaRepository<Cheater, Long> {
    fun existsByApplicant(applicant: Applicant): Boolean
}
