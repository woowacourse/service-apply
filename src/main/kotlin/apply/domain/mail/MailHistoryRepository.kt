package apply.domain.mail

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun MailHistoryRepository.getById(id: Long): MailHistory = findByIdOrNull(id)
    ?: throw NoSuchElementException("메일 이력이 존재하지 않습니다. id: $id")

interface MailHistoryRepository : JpaRepository<MailHistory, Long>
