package apply.application.mail

import apply.domain.mail.MailHistory
import support.createLocalDateTime
import java.time.LocalDateTime
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class MailData(
    @field:NotEmpty
    @field:Size(min = 1, max = 100)
    var subject: String = "",

    @field:NotEmpty
    var body: String = "",

    @field:NotEmpty
    @field:Size(min = 1, max = 100)
    var sender: String = "",

    @field:NotEmpty
    var recipients: List<String> = emptyList(),

    @field:NotNull
    var sentTime: LocalDateTime = createLocalDateTime(LocalDateTime.now()),

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
}
