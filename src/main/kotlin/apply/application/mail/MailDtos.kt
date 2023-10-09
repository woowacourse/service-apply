package apply.application.mail

import apply.domain.mail.MailMessage
import apply.domain.mail.MailReservation
import apply.domain.mail.MailReservationStatus
import java.time.LocalDateTime

data class MailMessageResponse(
    val id: Long,
    val subject: String,
    val body: String,
    val sender: String,
    val recipients: List<String>,
    val createdDateTime: LocalDateTime,
    val reservation: MailReservationResponse?
) {
    constructor(mailMessage: MailMessage, mailReservation: MailReservation? = null) : this(
        mailMessage.id,
        mailMessage.subject,
        mailMessage.body,
        mailMessage.sender,
        mailMessage.recipients,
        mailMessage.createdDateTime,
        mailReservation?.let { MailReservationResponse(it) }
    )
}

data class MailReservationResponse(
    val id: Long,
    val mailMessageId: Long,
    val status: MailReservationStatus,
    val reservationTime: LocalDateTime
) {
    constructor(mailReservation: MailReservation) : this(
        mailReservation.id,
        mailReservation.mailMessageId,
        mailReservation.status,
        mailReservation.reservationTime
    )
}
