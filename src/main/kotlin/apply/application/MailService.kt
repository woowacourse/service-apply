package apply.application

import apply.domain.applicant.ResetPasswordRequest
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.ISpringTemplateEngine

@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val mailProperties: MailProperties,
    private val applicationProperties: ApplicationProperties,
    private val templateEngine: ISpringTemplateEngine
) {
    @Async
    fun sendPasswordResetMail(request: ResetPasswordRequest, newPassword: String) {
        val context = Context().apply {
            setVariable("name", request.name)
            setVariable("password", newPassword)
            setVariable("url", applicationProperties.url)
        }

        val message = mailSender.createMimeMessage()
        MimeMessageHelper(message).apply {
            setFrom(mailProperties.username)
            setTo(request.email)
            setSubject("${request.name}님, 임시 비밀번호를 발송해 드립니다.")
            setText(templateEngine.process("mail/password-reset", context), true)
        }

        mailSender.send(message)
    }
}
