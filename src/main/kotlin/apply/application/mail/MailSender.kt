package apply.application.mail

import org.springframework.core.io.ByteArrayResource

interface MailSender {
    fun send(toAddress: String, subject: String, body: String)

    fun sendBcc(toAddresses: List<String>, subject: String, body: String, attachments: Map<String, ByteArrayResource>)
}
