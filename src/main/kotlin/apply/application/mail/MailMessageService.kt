package apply.application.mail

import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailReservation
import apply.domain.mail.MailReservationRepository
import apply.domain.mail.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MailMessageService(
    private val mailMessageRepository: MailMessageRepository,
    private val mailReservationRepository: MailReservationRepository,
) {
    fun reserve(request: MailData): MailMessageResponse {
        val mailMessage = mailMessageRepository.save(request.toMailMessage())
        val mailReservation = mailReservationRepository.save(
            MailReservation(mailMessage, reservationTime = request.sentTime)
        )
        return MailMessageResponse(mailMessage)
    }

    fun cancelReservation(mailMessageId: Long) {
        val mailMessage = mailMessageRepository.getOrThrow(mailMessageId)
        check(mailMessage.canDelete()) { "예약 취소할 수 없는 메일입니다." }
        mailMessageRepository.deleteById(mailMessageId)
    }
}
