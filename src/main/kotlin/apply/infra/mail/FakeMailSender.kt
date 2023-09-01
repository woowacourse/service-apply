package apply.infra.mail

import apply.application.mail.MailSender
import mu.KotlinLogging
import org.springframework.core.io.ByteArrayResource

private val logger = KotlinLogging.logger { }

class FakeMailSender : MailSender {
    override fun send(toAddress: String, subject: String, body: String) {
        logger.info("send mail: to - {}, subject - {}", toAddress, subject)
        logger.info("body: {}", body)
    }

    override fun sendBcc(
        toAddresses: List<String>,
        subject: String,
        body: String,
        attachments: Map<String, ByteArrayResource>
    ) {
        logger.info("send mail: to - {}, subject - {}", toAddresses, subject)
        logger.info("body: {}", body)
    }
}
