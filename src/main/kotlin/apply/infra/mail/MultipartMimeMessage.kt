package apply.infra.mail

import apply.application.ApplicationProperties
import com.amazonaws.services.simpleemail.model.RawMessage
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest
import org.springframework.core.io.ByteArrayResource
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.ISpringTemplateEngine
import org.thymeleaf.spring5.SpringTemplateEngine
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Properties
import javax.activation.DataHandler
import javax.activation.DataSource
import javax.activation.MimetypesFileTypeMap
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

data class Recipient(val recipientType: Message.RecipientType, val toAddresses: Array<String>) {
    constructor() : this(Message.RecipientType.BCC, arrayOf())
}

class MultipartMimeMessage(
    session: Session,
    private val mimeMixedPart: MimeMultipart,
    private val applicationProperties: ApplicationProperties,
    private val templateEngine: ISpringTemplateEngine
) {
    val message: MimeMessage = MimeMessage(session)

    fun setSubject(subject: String) {
        message.setSubject(subject, Charsets.UTF_8.name())
    }

    fun setFrom(userName: String) {
        message.setFrom(InternetAddress(userName))
    }

    fun setRecipient(recipient: Recipient) {
        message.setRecipients(recipient.recipientType, recipient.toAddresses.map { InternetAddress(it) }.toTypedArray())
    }

    fun addBody(body: String) {
        val messageBody = MimeMultipart("alternative")
        val wrap = MimeBodyPart()
        val context = Context().apply {
            setVariables(
                mapOf(
                    "email" to "email",
                    "title" to "title",
                    "content" to body,
                    "url" to applicationProperties.url
                )
            )
        }
        val htmlPart = MimeBodyPart()
        htmlPart.setContent(templateEngine.process("mail/common", context), "text/html; charset=UTF-8")
        messageBody.addBodyPart(htmlPart)
        wrap.setContent(messageBody)
        message.setContent(mimeMixedPart)
        mimeMixedPart.addBodyPart(wrap)
    }

    fun addAttachment(files: Map<String, ByteArrayResource>) {
        for ((fileName, fileData) in files) {
            val att = MimeBodyPart()
            val fds: DataSource = ByteArrayDataSource(
                fileData.byteArray,
                findMimeContentTypeByFileName(fileName)
            )
            att.dataHandler = DataHandler(fds)
            att.fileName = fileName
            mimeMixedPart.addBodyPart(att)
        }
    }

    private fun findMimeContentTypeByFileName(fileName: String): String {
        return MimetypesFileTypeMap().getContentType(fileName) ?: throw IllegalArgumentException("잘못된 확장자입니다.")
    }

    fun getRawEmailRequest(): SendRawEmailRequest {
        val rawMessage = createRawMessage(message)
        return SendRawEmailRequest(rawMessage)
    }

    private fun createRawMessage(message: MimeMessage): RawMessage {
        val outputStream = ByteArrayOutputStream()
        message.writeTo(outputStream)
        return RawMessage(ByteBuffer.wrap(outputStream.toByteArray()))
    }
}

data class MultipartMimeMessageBuilder(
    var session: Session = Session.getDefaultInstance(Properties()),
    var applicationProperties: ApplicationProperties = ApplicationProperties(""),
    var templateEngine: ISpringTemplateEngine = SpringTemplateEngine(),
    var mimeMixedPart: MimeMultipart = MimeMultipart("mixed"),
    var subject: String = "",
    var userName: String = "",
    var recipient: Recipient = Recipient(),
    var body: String = "",
    var files: Map<String, ByteArrayResource> = mutableMapOf()
) {
    fun build(): MultipartMimeMessage {
        return MultipartMimeMessage(session, mimeMixedPart, applicationProperties, templateEngine).apply {
            setSubject(subject)
            setFrom(userName)
            setRecipient(recipient)
            addBody(body)
            addAttachment(files)
        }
    }
}

fun message(lambda: MultipartMimeMessageBuilder.() -> Unit): MultipartMimeMessage {
    return MultipartMimeMessageBuilder().apply(lambda).build()
}