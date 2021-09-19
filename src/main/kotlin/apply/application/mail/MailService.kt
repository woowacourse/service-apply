package apply.application.mail

import apply.application.ApplicationProperties
import apply.application.RegisterUserRequest
import apply.application.ResetPasswordRequest
import apply.domain.applicant.Applicant
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
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
    fun sendFormSubmittedMail(applicant: Applicant) {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to applicant.name,
                    "url" to applicationProperties.url
                )
            )
        }
        mailSender.send(
            applicant.email,
            "${applicant.name}님, 지원이 완료되었습니다.",
            templateEngine.process("mail/submission-complete", context)
        )
    }

    @Async
    fun sendAuthenticationMail(request: RegisterUserRequest, authenticateCode: String) {
        val url = UriComponentsBuilder.fromUriString(applicationProperties.url)
            .path("/api/applicants/authenticate-email")
            .queryParam("email", request.email)
            .queryParam("authenticateCode", authenticateCode)
            .build()
            .toUriString()
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to request.name,
                    "url" to url
                )
            )
        }
        mailSender.send(
            request.email,
            "${request.name}님, 이메일 인증 메일 발송해 드립니다.",
            templateEngine.process("mail/email-authentication.html", context)
        )
    }
}
