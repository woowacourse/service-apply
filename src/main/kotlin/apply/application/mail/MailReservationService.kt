package apply.application.mail

import apply.domain.mail.MailReservationRepository
import apply.domain.mail.getOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class MailReservationService(
    private val mailReservationRepository: MailReservationRepository,
) {
    fun updateReservationTime(mailReservationId: Long, reservationTime: LocalDateTime): MailReservationResponse {
        val mailReservation = mailReservationRepository.getOrThrow(mailReservationId).apply {
            update(reservationTime)
        }

        return MailReservationResponse(mailReservationRepository.save(mailReservation))
    }

    fun deleteReservation(mailReservationId: Long) {
        val mailReservation = mailReservationRepository.getOrThrow(mailReservationId)
        mailReservation.validateStatus()
        mailReservationRepository.deleteById(mailReservation.id)
    }
}
