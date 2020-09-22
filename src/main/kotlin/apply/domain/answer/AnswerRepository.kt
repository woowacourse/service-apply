package apply.domain.answer

import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<Answer, Long> {
}