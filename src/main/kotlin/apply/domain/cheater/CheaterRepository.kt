package apply.domain.cheater

import apply.application.CheaterResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CheaterRepository : JpaRepository<Cheater, Long> {
    fun existsByApplicantId(applicantId: Long): Boolean

    @Query(
        """
        select new apply.application.CheaterResponse(c, a)
        from Applicant a inner join Cheater c on a.id = c.applicantId
        """
    )
    fun findAllByPage(pageable: Pageable): Page<CheaterResponse>
}
