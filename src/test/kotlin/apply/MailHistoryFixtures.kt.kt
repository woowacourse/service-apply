package apply

import apply.application.mail.MailData
import apply.application.mail.stringToList
import apply.domain.mail.MailHistory
import java.time.LocalDateTime

private const val SUBJECT: String = "메일제목"
private const val BODY: String = "메일 본문 입니다."
private const val SENDER: String = "woowacourse@email.com"
private const val RECIPIENTS: String = "test1@email.com,test2@email.com"
private val SENT_TIME: LocalDateTime = LocalDateTime.now()

fun createMailHistory(
    subject: String = SUBJECT,
    body: String = BODY,
    sender: String = SENDER,
    recipients: String = RECIPIENTS,
    sentTime: LocalDateTime = SENT_TIME,
    id: Long = 0L
): MailHistory {
    return MailHistory(subject, body, sender, recipients, sentTime, id)
}

fun createMailData(
    subject: String = SUBJECT,
    body: String = BODY,
    sender: String = SENDER,
    recipients: List<String> = RECIPIENTS.stringToList(),
    sentTime: LocalDateTime = SENT_TIME,
    id: Long = 0L
): MailData {
    return MailData(subject, body, sender, recipients, sentTime, id)
}
