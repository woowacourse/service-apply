package apply.domain.mail

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun EmailHistoryRepository.getById(id: Long): MailHistory = findByIdOrNull(id) ?: throw NoSuchElementException()

interface EmailHistoryRepository : JpaRepository<MailHistory, Long>
