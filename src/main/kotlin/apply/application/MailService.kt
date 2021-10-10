package apply.application

import apply.application.mail.MailData
import apply.domain.mail.MailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.getById
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailService(
    private val mailHistoryRepository: MailHistoryRepository,
    private val userRepository: UserRepository
) {
    fun save(mailData: MailData) {
        mailHistoryRepository.save(
            MailHistory(
                mailData.subject,
                mailData.body,
                mailData.sender,
                mailData.recipients,
                mailData.sentTime
            )
        )
    }

    fun findAll(): List<MailData> {
        return mailHistoryRepository.findAll().map { MailData(it) }
    }

    fun getById(emailHistoryId: Long): MailData {
        val emailHistory = mailHistoryRepository.getById(emailHistoryId)
        return MailData(emailHistory)
    }

    fun findMailTargetByEmail(email: String): MailTargetResponse {
        val user = userRepository.findByEmail(email) ?: return MailTargetResponse(email = email)
        return MailTargetResponse(user.name, user.email)
    }
}
