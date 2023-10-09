package apply.application.mail

import apply.domain.mail.MailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailSentEvent
import apply.domain.mail.getOrThrow
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailHistoryService(
    private val mailHistoryRepository: MailHistoryRepository,
    private val mailMessageRepository: MailMessageRepository
) {
    @EventListener
    fun onMailSentEvent(event: MailSentEvent) {
        val mailData = event.mailData
        val mailMessage = mailMessageRepository.findById(mailData.id)
            .let { mailMessageRepository.save(mailData.toMailMessage()) }

        val mailHistories = mutableListOf<MailHistory>()
        if (event.succeedRecipients.isNotEmpty()) {
            mailHistories.add(MailHistory(mailMessage, event.succeedRecipients, true))
        }
        if (event.failedRecipients.isNotEmpty()) {
            mailHistories.add(MailHistory(mailMessage, event.failedRecipients, false))
        }

        mailHistoryRepository.saveAll(mailHistories)
    }

    fun findAll(): List<MailData> {
        return mailHistoryRepository.findAll().map { MailData(it) }
    }

    fun getById(mailHistoryId: Long): MailData {
        val mailHistory = mailHistoryRepository.getOrThrow(mailHistoryId)
        return MailData(mailHistory)
    }
}
