package apply.domain.answer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import javax.transaction.Transactional

interface ApplicantAnswerRepository : JpaRepository<ApplicantAnswer, Long> {
    fun findAllByApplicationFormId(applicationFormId: Long) : ArrayList<ApplicantAnswer>

    @Transactional
    @Modifying
    @Query("delete from ApplicantAnswer a where a.applicationFormId = ?1")
    fun deleteAllByApplicationFormId(applicationFormId: Long) : Int
}