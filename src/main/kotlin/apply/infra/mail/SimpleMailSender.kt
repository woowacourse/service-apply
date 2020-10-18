package apply.infra.mail

import apply.application.mail.MailSender
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

class SimpleMailSender(
    private val mailProperties: MailProperties,
    private val mailSender: JavaMailSender
) : MailSender {
    override fun send(toAddress: String, subject: String, body: String) {
        val message = mailSender.createMimeMessage()
        MimeMessageHelper(message).apply {
            setFrom(mailProperties.username)
            setTo(toAddress)
            setSubject(subject)
            setText(body, true)
        }
        mailSender.send(message)
    }
}
