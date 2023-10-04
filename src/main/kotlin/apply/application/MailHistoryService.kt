package apply.application

import apply.application.mail.MailData
import apply.domain.mail.MailHistory2Repository
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailHistoryService(
    private val mailHistoryRepository: MailHistoryRepository,
    private val mailHistory2Repository: MailHistory2Repository
) {
    fun findAll(): List<MailData> {
        return mailHistory2Repository.findAll().map { MailData(it) }
    }

    fun getById(mailHistoryId: Long): MailData {
        val mailHistory = mailHistory2Repository.getOrThrow(mailHistoryId)
        return MailData(mailHistory)
    }
}
