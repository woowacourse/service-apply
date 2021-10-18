package apply.domain.mail

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun MailHistoryRepository.getById(id: Long): MailHistory {
    return findByIdOrNull(id) ?: throw NoSuchElementException("해당 메일 이력은 존재하지 않습니다.")
}

interface MailHistoryRepository : JpaRepository<MailHistory, Long>
