package apply.application.mail

import apply.application.ApplicationProperties
import apply.application.ResetPasswordRequest
import apply.domain.user.User
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.ISpringTemplateEngine

@Service
class MailService(
    private val applicationProperties: ApplicationProperties,
    private val templateEngine: ISpringTemplateEngine,
    private val mailSender: MailSender
) {
    @Async
    fun sendPasswordResetMail(request: ResetPasswordRequest, newPassword: String) {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to request.name,
                    "password" to newPassword,
                    "url" to applicationProperties.url
                )
            )
        }
        mailSender.send(
            request.email,
            "${request.name}님, 임시 비밀번호를 발송해 드립니다.",
            templateEngine.process("mail/password-reset", context)
        )
    }

    @Async
    fun sendFormSubmittedMail(user: User) {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to user.name,
                    "url" to applicationProperties.url
                )
            )
        }
        mailSender.send(
            user.email,
            "${user.name}님, 지원이 완료되었습니다.",
            templateEngine.process("mail/submission-complete", context)
        )
    }

    @Async
    fun sendAuthenticationCodeMail(email: String, authenticateCode: String) {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "authenticationCode" to authenticateCode
                )
            )
        }
        mailSender.send(
            email,
            "메일 인증 코드를 발송해 드립니다.",
            templateEngine.process("mail/email-authentication.html", context)
        )
    }
}
