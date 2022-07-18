package apply.application.mail

import apply.application.ApplicationProperties
import apply.domain.applicationform.ApplicationFormSubmittedEvent
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getById
import apply.domain.user.PasswordResetEvent
import apply.domain.user.UserRepository
import apply.domain.user.getById
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ByteArrayResource
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.ISpringTemplateEngine

private const val MAIL_SENDING_UNIT = 50

@Service
class MailService(
    private val userRepository: UserRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val applicationProperties: ApplicationProperties,
    private val templateEngine: ISpringTemplateEngine,
    private val mailSender: MailSender,
    private val mailProperties: MailProperties
) {
    @Async
    @TransactionalEventListener
    fun sendPasswordResetMail(event: PasswordResetEvent) {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to event.name,
                    "tempPassword" to event.password,
                    "loginPageUrl" to "${applicationProperties.url}/login",
                    "url" to applicationProperties.url
                )
            )
        }
        mailSender.send(
            event.email,
            "${event.name}님, 임시 비밀번호를 발송해 드립니다.",
            templateEngine.process("mail/password-reset", context)
        )
    }

    @Async
    @TransactionalEventListener
    fun sendFormSubmittedMail(event: ApplicationFormSubmittedEvent) {
        val user = userRepository.getById(event.userId)
        val recruitment = recruitmentRepository.getById(event.recruitmentId)
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to user.name,
                    "recruit" to recruitment.title,
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
    fun sendAuthenticationCodeMail(email: String, authenticationCode: String) {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "authenticationCode" to authenticationCode,
                    "url" to applicationProperties.url
                )
            )
        }
        mailSender.send(
            email,
            "메일 인증 코드를 발송해 드립니다.",
            templateEngine.process("mail/email-authentication.html", context)
        )
    }

    @Async
    fun sendMailsByBcc(request: MailData, files: Map<String, ByteArrayResource>) {
        request.recipients.plus(mailProperties.username)
        for (targetMailsPart in request.recipients.chunked(MAIL_SENDING_UNIT)) {
            mailSender.sendBcc(targetMailsPart, request.subject, request.body, files)
        }
    }
}
