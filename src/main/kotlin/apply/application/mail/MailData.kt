package apply.application.mail

import javax.validation.constraints.NotEmpty

data class MailData(
    @field:NotEmpty
    var subject: String = "",

    @field:NotEmpty
    var body: String = "",

    @field:NotEmpty
    var recipients: List<String> = emptyList()
)
