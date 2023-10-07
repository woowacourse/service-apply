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

    fun cancelReservation(mailMessageId: Long) {
        val mailMessage = mailMessageRepository.getOrThrow(mailMessageId)
        check(mailMessage.canDelete()) { "예약 취소할 수 없는 메일입니다." }
        mailMessageRepository.deleteById(mailMessageId)
    }
}
