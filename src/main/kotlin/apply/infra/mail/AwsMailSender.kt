package apply.infra.mail

import apply.application.mail.MailSender
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Component
import javax.mail.Message as javaxMessage

@Component
class AwsMailSender(
    private val mailProperties: MailProperties,
    awsProperties: AwsProperties
) : MailSender {
    private val client: AmazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder
        .standard()
        .withCredentials(
            AWSStaticCredentialsProvider(
                BasicAWSCredentials(
                    awsProperties.accessKey,
                    awsProperties.secretKey
                )
            )
        )
        .withRegion(Regions.AP_NORTHEAST_2)
        .build()

    override fun send(toAddress: String, subject: String, body: String) {
        val request = SendEmailRequest()
            .withSource(mailProperties.username)
            .withDestination(Destination().withToAddresses(toAddress))
            .withMessage(
                Message()
                    .withSubject(createContent(subject))
                    .withBody(Body().withHtml(createContent(body)))
            )
        client.sendEmail(request)
    }

    override fun sendBcc(
        toAddresses: Array<String>,
        subject: String,
        body: String,
        files: Map<String, ByteArrayResource>
    ) {
        val multipartMimeMessage = message {
            this.subject = subject
            this.userName = mailProperties.username
            this.recipient = Recipient(javaxMessage.RecipientType.BCC, toAddresses)
            this.body = body
            this.files = files
        }.build()

        val rawEmailRequest = multipartMimeMessage.getRawEmailRequest()
        try {
            client.sendRawEmail(rawEmailRequest)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createContent(data: String): Content {
        return Content(data).withCharset("UTF-8")
    }
}
