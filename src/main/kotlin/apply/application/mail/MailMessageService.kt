package apply.application.mail

import apply.domain.mail.MailMessageRepository
import apply.domain.mail.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailMessageService(
    private val mailMessageRepository: MailMessageRepository
) {
    fun reserve(request: MailData): MailMessageResponse {
        val mailMessage = mailMessageRepository.save(request.toReservationMailMessage())
        return MailMessageResponse(mailMessage)
    }

    fun updateReservedMessage(mailMessageId: Long, request: MailData) {
        val mailMessage = mailMessageRepository.getOrThrow(mailMessageId)
        mailMessage.update(request.subject, request.body, request.recipients)
        mailMessageRepository.save(mailMessage)
    }
}
