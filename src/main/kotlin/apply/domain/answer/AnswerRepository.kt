package apply.domain.answer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import javax.transaction.Transactional

interface AnswerRepository : JpaRepository<Answer, Long> {
    fun findAllByApplicationFormId(applicationFormId: Long) : ArrayList<Answer>

    @Transactional
    @Modifying
    @Query("delete from Answer a where a.applicationFormId = ?1")
    fun deleteAllByApplicationFormId(applicationFormId: Long) : Int
}