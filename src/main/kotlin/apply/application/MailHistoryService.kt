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
    fun save(request: MailData) {
        mailHistoryRepository.save(
            MailHistory(
                request.subject,
                request.body,
                request.sender,
                request.recipients,
                request.sentTime
            )
        )
    }

    fun findAll(): List<MailData> {
        return mailHistoryRepository.findAll().map { MailData(it) }
    }

    fun getById(mailHistoryId: Long): MailData {
        val mailHistory = mailHistoryRepository.getById(mailHistoryId)
        return MailData(mailHistory)
    }

    fun findAllMailTargetsByEmails(emails: List<String>): List<MailTargetResponse> {
        val users = userRepository.findAllByEmailIn(emails)
        val anonymousEmails = emails - users.map { it.email }
        return users.map { MailTargetResponse(it) } + anonymousEmails.map { MailTargetResponse(it) }
    }
}
