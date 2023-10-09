package apply.application.mail

import apply.domain.mail.MailMessageRepository
import apply.domain.mail.MailReservationRepository
import apply.domain.mail.MailReservationStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class MailReservationService(
    private val mailService: MailService,
    private val mailReservationRepository: MailReservationRepository,
    private val mailMessageRepository: MailMessageRepository
) {
    fun findByWaitingStatus(): List<MailReservationResponse> {
        return mailReservationRepository.findByStatus(MailReservationStatus.WAITING)
            .map { MailReservationResponse(it) }
    }

    fun sendMail(standardTime: LocalDateTime = LocalDateTime.now()) {
        val reservations = mailReservationRepository.findByReservationTimeBetweenAndStatus(
            standardTime.minusMinutes(1),
            standardTime.plusMinutes(1),
            MailReservationStatus.WAITING
        )
        val messagesById = mailMessageRepository
            .findAllById(reservations.map { it.mailMessageId })
            .associateBy { it.id }

        reservations.forEach { mailReservation ->
            mailReservation.send()
            mailService.sendMailsByBcc(MailData(messagesById.getValue(mailReservation.id)), emptyMap()) {
                mailReservation.finish()
            }
        }
    }
}
