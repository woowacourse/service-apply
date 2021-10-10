package apply.application.mail

import apply.domain.mail.MailHistory
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
    constructor(mailHistory: MailHistory) : this(
        mailHistory.subject,
        mailHistory.body,
        mailHistory.sender,
        mailHistory.recipients,
        mailHistory.sentTime,
        mailHistory.id
    )

    fun recipientsCount(): Int {
        return recipients.size
    }
}
