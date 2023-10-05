package apply.domain.mail

import apply.application.mail.MailData

data class MailSentEvent(
    val mailData: MailData,
    val succeedRecipients: List<String>,
    val failedRecipients: List<String>
)
