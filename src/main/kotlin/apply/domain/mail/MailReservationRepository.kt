package apply.domain.mail

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

fun MailReservationRepository.getOrThrow(id: Long) = findByIdOrNull(id)
    ?: throw NoSuchElementException("메일 예약이 존재하지 않습니다. id: $id")

interface MailReservationRepository : JpaRepository<MailReservation, Long> {
    fun findByStatus(status: MailReservationStatus): List<MailReservation>
    fun findByReservationTimeBetweenAndStatus(
        from: LocalDateTime,
        to: LocalDateTime,
        status: MailReservationStatus
    ): List<MailReservation>

    fun findByMailMessageId(mailMessageId: Long): MailReservation?
}
