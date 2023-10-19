package apply.application.mail

import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.MailMessageRepository
import apply.domain.mail.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailHistoryService(
    private val mailHistoryRepository: MailHistoryRepository,
    private val mailMessageRepository: MailMessageRepository
) {
    fun findAll(): List<MailData> {
        val histories = mailHistoryRepository.findAll()
        val messagesById = mailMessageRepository.findAllById(histories.map { it.mailMessageId }).associateBy { it.id }
        return histories.map { MailData(messagesById.getValue(it.mailMessageId), it) }
    }

    fun getById(mailHistoryId: Long): MailData {
        val mailHistory = mailHistoryRepository.getOrThrow(mailHistoryId)
        val mailMessage = mailMessageRepository.getOrThrow(mailHistory.mailMessageId)
        return MailData(mailMessage, mailHistory)
    }
}
