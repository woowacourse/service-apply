package apply.domain.applicant

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ApplicantRepository : JpaRepository<Applicant, Long> {
    @Query("select a from Applicant a where a.information.name like %:keyword% or a.information.email like %:keyword%")
    fun findAllByKeyword(@Param("keyword") keyword: String): List<Applicant>

    @Query("select a from Applicant a where a.information.email = :email")
    fun findByEmail(@Param("email") email: String): Applicant?

    @Query("select a from Applicant a where a.information.email in :emails")
    fun findAllByEmailIn(@Param("emails") map: List<String>): List<Applicant>
}
