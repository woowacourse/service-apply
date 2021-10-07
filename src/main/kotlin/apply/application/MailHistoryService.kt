package apply.application

import apply.application.mail.MailData
import apply.domain.mail.MailHistory
import apply.domain.mail.EmailHistoryRepository
import apply.domain.mail.getById
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailHistoryService(
    private val emailHistoryRepository: EmailHistoryRepository
) {
    fun save(mailData: MailData) {
        emailHistoryRepository.save(
            MailHistory(
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
                it.recipients,
                it.sentTime,
                it.id
            )
        }
    }

    fun findById(emailHistoryId: Long): MailData {
        val emailHistory = emailHistoryRepository.getById(emailHistoryId)
        return MailData(emailHistory)
    }
}
