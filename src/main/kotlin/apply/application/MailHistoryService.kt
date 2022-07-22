package apply.application

import apply.application.mail.MailData
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.getById
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailHistoryService(
    private val mailHistoryRepository: MailHistoryRepository
) {
    fun findAll(): List<MailData> {
        return mailHistoryRepository.findAll().map { MailData(it) }
    }

    fun getById(mailHistoryId: Long): MailData {
        val mailHistory = mailHistoryRepository.getById(mailHistoryId)
        return MailData(mailHistory)
    }
}
