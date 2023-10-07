package apply.application.mail

import apply.domain.mail.MailMessageRepository
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
}
