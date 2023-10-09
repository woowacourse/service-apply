package apply

import apply.application.mail.MailData
import apply.domain.mail.MailHistory
import apply.domain.mail.MailMessage
import apply.domain.mail.MailReservation
import java.time.LocalDateTime

private const val SUBJECT: String = "메일제목"
private const val BODY: String = "메일 본문 입니다."
private const val SENDER: String = "woowacourse@email.com"
private const val MAIL_MESSAGE_ID: Long = 1L
private val RECIPIENTS: List<String> = listOf("test1@email.com", "test2@email.com")
private val SENT_TIME: LocalDateTime = LocalDateTime.now()
private val RESERVATION_TIME: LocalDateTime = LocalDateTime.now().plusHours(3).withMinute(0)

fun createMailData(
    subject: String = SUBJECT,
    body: String = BODY,
    sender: String = SENDER,
    recipients: List<String> = RECIPIENTS,
    sentTime: LocalDateTime = SENT_TIME,
    id: Long = 0L
): MailData {
    return MailData(subject, body, sender, recipients, sentTime, id = id)
}

fun createMailMessage(
    subject: String = SUBJECT,
    body: String = BODY,
    sender: String = SENDER,
    recipients: List<String> = RECIPIENTS,
    id: Long = 0L,
    creatorId: Long = 0L
): MailMessage {
    return MailMessage(subject, body, sender, recipients, creatorId, id = id)
}

fun createMailReservation(
    mailMessageId: Long = MAIL_MESSAGE_ID,
    reservationTime: LocalDateTime = RESERVATION_TIME,
    id: Long = 0L,
): MailReservation {
    return MailReservation(mailMessageId, reservationTime = reservationTime, id = id)
}

fun createSuccessMailHistory(
    mailMessageId: Long = MAIL_MESSAGE_ID,
    recipients: List<String> = RECIPIENTS
): MailHistory {
    return MailHistory(mailMessageId, recipients, true)
}
