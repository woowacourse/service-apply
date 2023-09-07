package apply.infra.mail

import apply.application.mail.MailSender
import mu.KotlinLogging
import org.springframework.core.io.ByteArrayResource

private val logger = KotlinLogging.logger { }

class FakeMailSender : MailSender {
    override fun send(toAddress: String, subject: String, body: String) {
        logger.debug { "send mail: to - $toAddress, subject - $subject" }
        logger.debug { "body: $body" }
    }

    override fun sendBcc(
        toAddresses: List<String>,
        subject: String,
        body: String,
        attachments: Map<String, ByteArrayResource>
    ) {
        logger.debug { "send mail: to - $toAddresses, subject - $subject" }
        logger.debug { "body: $body" }
    }
}
