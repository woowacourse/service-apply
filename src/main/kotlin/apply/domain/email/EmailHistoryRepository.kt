package apply.domain.email

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun EmailHistoryRepository.getById(id: Long): EmailHistory = findByIdOrNull(id) ?: throw NoSuchElementException()

interface EmailHistoryRepository : JpaRepository<EmailHistory, Long>
