package apply.application.mail

import apply.domain.email.EmailHistory
import apply.utils.DELIMITER
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class MailData(
    @field:NotEmpty
    var subject: String = "",

    @field:NotEmpty
    var body: String = "",

    @field:NotEmpty
    var sender: String = "",

    @field:NotEmpty
    var recipients: List<String> = emptyList(),

    @field:NotNull
    var sentTime: LocalDateTime = LocalDateTime.now(),

    @field:NotNull
    var id: Long = 0L
) {
    constructor(
        subject: String,
        body: String,
        sender: String,
        recipients: String,
        sentTime: LocalDateTime,
        id: Long
    ) : this(subject, body, sender, recipients.stringToList(), sentTime, id)

    constructor(emailHistory: EmailHistory) : this(
        emailHistory.subject,
        emailHistory.body,
        emailHistory.sender,
        emailHistory.recipients.stringToList(),
        emailHistory.sentTime,
        emailHistory.id
    )

    fun recipientsCount(): Int {
        return recipients.size
    }

    fun recipientsToString(): String {
        return recipients.listToString()
    }
}

fun List<String>.listToString(): String {
    return this.joinToString(DELIMITER)
}

fun String.stringToList(): List<String> {
    return this.split(DELIMITER)
}
