package apply.application.mail

import javax.validation.constraints.NotNull

data class MailSendData(
    @field:NotNull
    var subject: String = "",
    @field:NotNull
    var content: String = "",
    @field:NotNull
    var targetMails: List<String> = listOf()
)
