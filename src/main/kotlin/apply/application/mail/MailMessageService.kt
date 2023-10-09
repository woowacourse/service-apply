package apply.application.mail

import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailReservation
import apply.domain.mail.MailReservationRepository
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
            MailReservation(mailMessageId = mailMessage.id, reservationTime = request.sentTime)
        )
        return MailMessageResponse(mailMessage, mailReservation)
    }

    fun cancelReservation(mailMessageId: Long) {
        val mailReservation = mailReservationRepository.findByMailMessageId(mailMessageId)
            ?: throw IllegalArgumentException("메일 예약이 존재하지 않습니다. email: $mailMessageId")
        check(mailReservation.canCancel()) { "예약 취소할 수 없는 메일입니다." }
        mailReservationRepository.deleteById(mailReservation.id)
        mailMessageRepository.deleteById(mailReservation.mailMessageId)
    }
}
