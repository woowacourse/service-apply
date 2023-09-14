package apply.domain.mail

import java.time.LocalDateTime

class MailMessage private constructor(
    val subject: String,
    val body: String,
    val sender: String,
    val recipients: List<String>,
    val creatorId: Long,
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    modifiedDateTime: LocalDateTime = LocalDateTime.now(),
) {

    var reservation: MailReservation? = null

    var modifiedDateTime: LocalDateTime = modifiedDateTime
        private set

    companion object {
        fun of(
            subject: String,
            body: String,
            sender: String,
            recipients: List<String>,
            creatorId: Long
        ): MailMessage {
            return MailMessage(
                subject = subject,
                body = body,
                sender = sender,
                recipients = recipients,
                creatorId = creatorId
            )
        }

        fun withReservation(
            subject: String,
            body: String,
            sender: String,
            recipients: List<String>,
            reservationTime: LocalDateTime,
            creatorId: Long
        ): MailMessage {
            return MailMessage(
                subject = subject,
                body = body,
                sender = sender,
                recipients = recipients,
                creatorId = creatorId
            ).apply {
                this.reservation = MailReservation()
            }
        }
    }
}
