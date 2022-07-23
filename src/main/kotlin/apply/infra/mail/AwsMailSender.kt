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
import com.amazonaws.services.simpleemail.model.RawMessage
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Component
import support.infra.RateLimiter
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Properties
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.MimetypesFileTypeMap
import javax.mail.Message.RecipientType
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

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

    private val rateLimiter = RateLimiter(14)

    override fun send(toAddress: String, subject: String, body: String) {
        rateLimiter.acquire()
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
        toAddresses: List<String>,
        subject: String,
        body: String,
        attachments: Map<String, ByteArrayResource>
    ) {
        rateLimiter.acquire()
        val multipartMimeMessage = MultipartMimeMessage(
            subject = subject,
            userName = mailProperties.username,
            recipient = toAddresses,
            body = body,
            files = attachments
        )
        val rawEmailRequest = multipartMimeMessage.getRawEmailRequest()
        client.sendRawEmail(rawEmailRequest) // TODO MessageRejectedException 처리
    }

    private fun createContent(data: String): Content {
        return Content(data).withCharset(Charsets.UTF_8.name())
    }
}

private class MultipartMimeMessage(
    val message: MimeMessage = MimeMessage(Session.getDefaultInstance(Properties()))
) {
    constructor(
        subject: String,
        userName: String,
        recipient: List<String>,
        body: String,
        files: Map<String, ByteArrayResource>
    ) : this() {
        val mimeMultipart = MimeMultipart("mixed")
        setSubject(subject)
        setFrom(userName)
        setRecipient(recipient)
        addBody(body, mimeMultipart)
        addAttachment(files, mimeMultipart)
    }

    fun setSubject(subject: String) {
        message.setSubject(subject, Charsets.UTF_8.name())
    }

    fun setFrom(userName: String) {
        message.setFrom(InternetAddress(userName))
    }

    fun setRecipient(recipient: List<String>) {
        message.setRecipients(
            RecipientType.BCC,
            recipient.map { InternetAddress(it) }.toTypedArray()
        )
    }

    fun addBody(body: String, mimeMixedPart: MimeMultipart) {
        val messageBody = MimeMultipart("alternative")
        val wrap = MimeBodyPart()
        val htmlPart = MimeBodyPart()
        htmlPart.setContent(body, "text/html; charset=UTF-8")
        messageBody.addBodyPart(htmlPart)
        wrap.setContent(messageBody)
        message.setContent(mimeMixedPart)
        mimeMixedPart.addBodyPart(wrap)
    }

    fun addAttachment(files: Map<String, ByteArrayResource>, mimeMixedPart: MimeMultipart) {
        for ((fileName, fileData) in files) {
            val bodyPart = MimeBodyPart()
            val fds: DataSource = ByteArrayDataSource(
                fileData.byteArray,
                findMimeContentTypeByFileName(fileName)
            )
            bodyPart.dataHandler = DataHandler(fds)
            bodyPart.fileName = fileName
            mimeMixedPart.addBodyPart(bodyPart)
        }
    }

    fun findMimeContentTypeByFileName(fileName: String): String {
        return MimetypesFileTypeMap().getContentType(fileName) ?: throw IllegalArgumentException("잘못된 확장자입니다.")
    }

    fun getRawEmailRequest(): SendRawEmailRequest {
        val rawMessage = createRawMessage(message)
        return SendRawEmailRequest(rawMessage)
    }

    fun createRawMessage(message: MimeMessage): RawMessage {
        ByteArrayOutputStream().use { outputStream ->
            message.writeTo(outputStream)
            return RawMessage(ByteBuffer.wrap(outputStream.toByteArray()))
        }
    }
}
