package apply.application

import apply.application.mail.MailData
import apply.domain.mail.MailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.getById
import apply.domain.user.UserRepository
import apply.domain.user.findAllByEmailIn
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class MailHistoryService(
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

    fun findAllMailTargetsByEmails(emails: List<String>): List<MailTargetResponse> {
        val users = userRepository.findAllByEmailIn(emails)
        val anonymousEmails = emails.toSet() - users.map { it.email }.toSet()
        return users.map { MailTargetResponse(it) } + anonymousEmails.map { MailTargetResponse(it) }
    }
}
