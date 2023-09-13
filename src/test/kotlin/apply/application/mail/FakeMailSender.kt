package apply.application.mail

import mu.KotlinLogging
import org.springframework.core.io.ByteArrayResource

private val logger = KotlinLogging.logger {}

class FakeMailSender : MailSender {
    override fun send(toAddress: String, subject: String, body: String) {
        logger.debug { "toAddress: $toAddress, subject: $subject" }
        logger.debug { "body: $body" }
    }

    override fun sendBcc(
        toAddresses: List<String>,
        subject: String,
        body: String,
        attachments: Map<String, ByteArrayResource>
    ) {
        logger.debug { "toAddresses: $toAddresses, subject: $subject" }
        logger.debug { "body: $body" }
    }
}
