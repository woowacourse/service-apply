package apply.application

import apply.application.mail.MailData
import apply.domain.email.EmailHistory
import apply.domain.email.EmailHistoryRepository
import apply.utils.DELIMITER
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailHistoryService(
    private val emailHistoryRepository: EmailHistoryRepository
) {
    fun save(mailData: MailData) {
        emailHistoryRepository.save(
            EmailHistory(
                mailData.subject,
                mailData.body,
                mailData.sender,
                mailData.recipientsToString(),
                mailData.sentTime
            )
        )
    }

    fun findAll(): List<MailData> {
        val emailHistory = emailHistoryRepository.findAll()
        return emailHistory.map {
            MailData(
                it.subject,
                it.body,
                it.sender,
                it.recipients.toList(),
                it.sentTime,
                it.id
            )
        }
    }

    private fun String.toList(): List<String> {
        return this.split(DELIMITER)
    }
}
