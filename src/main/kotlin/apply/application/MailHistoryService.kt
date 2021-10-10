package apply.application

import apply.application.mail.MailData
import apply.domain.mail.MailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.getById
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailHistoryService(
    private val mailHistoryRepository: MailHistoryRepository
) {
    fun save(mailData: MailData) {
        mailHistoryRepository.save(
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
        val emailHistory = mailHistoryRepository.findAll()
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

    fun getById(emailHistoryId: Long): MailData {
        val emailHistory = mailHistoryRepository.getById(emailHistoryId)
        return MailData(emailHistory)
    }
}
