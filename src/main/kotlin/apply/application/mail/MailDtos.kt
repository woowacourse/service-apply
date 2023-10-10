package apply.application.mail

import apply.domain.mail.MailHistory
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
    val sentTime: LocalDateTime?,
    val reservation: MailReservationResponse?,
    val histories: List<MailHistoryResponse>
) {
    constructor(
        mailMessage: MailMessage,
        mailReservation: MailReservation? = null,
        mailHistories: List<MailHistory> = emptyList()
    ) : this(
        mailMessage.id,
        mailMessage.subject,
        mailMessage.body,
        mailMessage.sender,
        mailMessage.recipients,
        mailMessage.createdDateTime,
        mailHistories.firstOrNull()?.sentTime,
        mailReservation?.let { MailReservationResponse(it) },
        mailHistories.map { MailHistoryResponse(it) }
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

data class MailHistoryResponse(
    val id: Long,
    val mailMessageId: Long,
    val recipients: List<String>,
    val success: Boolean,
    val sentTime: LocalDateTime
) {
    constructor(mailHistory: MailHistory) : this(
        mailHistory.id,
        mailHistory.mailMessageId,
        mailHistory.recipients,
        mailHistory.success,
        mailHistory.sentTime
    )
}
