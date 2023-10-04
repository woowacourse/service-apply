package apply.domain.mail

import java.time.LocalDateTime

class MailHistory2 private constructor(
    val mailMessage: MailMessage,
    val recipients: List<String>,
    val success: Boolean,
    val sentTime: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        fun ofSuccess(mailMessage: MailMessage, recipients: List<String>): MailHistory2 {
            return MailHistory2(mailMessage, recipients, true)
        }

        fun ofFailure(mailMessage: MailMessage, recipients: List<String>): MailHistory2 {
            return MailHistory2(mailMessage, recipients, false)
        }
    }
}
