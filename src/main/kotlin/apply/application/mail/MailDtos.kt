package apply.application.mail

import apply.domain.mail.MailMessage
import apply.domain.mail.MailReservation
import apply.domain.mail.MailReservationStatus
import java.time.LocalDateTime

data class MailMessageResponse(
    val subject: String,
    val body: String,
    val sender: String,
    val recipients: List<String>,
    val createdDateTime: LocalDateTime,
    val modifiedDateTime: LocalDateTime,
    val reservation: MailReservationSimpleResponse?,
    val id: Long
) {
    constructor(mailMessage: MailMessage) : this(
        mailMessage.subject,
        mailMessage.body,
        mailMessage.sender,
        mailMessage.recipients,
        mailMessage.createdDateTime,
        mailMessage.modifiedDateTime,
        mailMessage.reservation()?.let { MailReservationSimpleResponse(it) },
        mailMessage.id
    )
}

data class MailReservationSimpleResponse(
    val status: MailReservationStatus,
    val reservationTime: LocalDateTime,
    val id: Long,
) {
    constructor(mailReservation: MailReservation) : this(
        mailReservation.status,
        mailReservation.reservationTime,
        mailReservation.id
    )
}
