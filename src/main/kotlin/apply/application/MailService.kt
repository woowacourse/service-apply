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
    private val customApplicationProperties: CustomApplicationProperties,
    private val templateEngine: ISpringTemplateEngine
) {
    @Async
    fun sendPasswordResetMail(request: ResetPasswordRequest, newPassword: String) {
        val context = Context().apply {
            setVariable("name", request.name)
            setVariable("password", newPassword)
            setVariable("url", customApplicationProperties.url)
        }

        val message = mailSender.createMimeMessage()
        val messageHelper = MimeMessageHelper(message)
        messageHelper.setFrom(mailProperties.username)
        messageHelper.setTo(request.email)
        messageHelper.setSubject("${request.name}님, 임시 비밀번호를 발송해 드립니다.")
        messageHelper.setText(templateEngine.process("mail/password-reset", context), true)

        mailSender.send(message)
    }
}
