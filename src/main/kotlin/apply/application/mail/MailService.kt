package apply.application.mail

import apply.application.ApplicationProperties
import apply.domain.applicationform.ApplicationFormSubmittedEvent
import apply.domain.mail.MailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.MailMessageRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitment.getOrThrow
import apply.domain.user.PasswordResetEvent
import apply.domain.user.UserRepository
import apply.domain.user.getOrThrow
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.io.ByteArrayResource
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.event.TransactionalEventListener
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.ISpringTemplateEngine
import support.markdownToEmbeddedHtml

private const val MAIL_SENDING_UNIT: Int = 50

@Service
class MailService(
    private val userRepository: UserRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val mailMessageRepository: MailMessageRepository,
    private val mailHistoryRepository: MailHistoryRepository,
    private val applicationProperties: ApplicationProperties,
    private val templateEngine: ISpringTemplateEngine,
    private val mailSender: MailSender,
    private val mailProperties: MailProperties,
    private val eventPublisher: ApplicationEventPublisher,
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
        val user = userRepository.getOrThrow(event.userId)
        val recruitment = recruitmentRepository.getOrThrow(event.recruitmentId)
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
        val mailMessage = mailMessageRepository.save(request.toMailMessage())
        val body = generateMailBody(request)
        val recipients = request.recipients + mailProperties.username

        val succeeded = mutableListOf<String>()
        val failed = mutableListOf<String>()
        for (addresses in recipients.chunked(MAIL_SENDING_UNIT)) {
            runCatching { mailSender.sendBcc(addresses, request.subject, body, files) }
                .onSuccess { succeeded.addAll(addresses) }
                .onFailure { failed.addAll(addresses) }
        }

        saveMailHistories(mailMessage.id, succeeded, failed)
    }

    fun sendMailsByBccSynchronous(request: MailData, files: Map<String, ByteArrayResource>) {
        val mailMessage = mailMessageRepository.save(request.toMailMessage())
        val body = generateMailBody(request)
        val recipients = mailMessage.recipients + mailProperties.username

        val succeeded = mutableListOf<String>()
        val failed = mutableListOf<String>()
        for (addresses in recipients.chunked(MAIL_SENDING_UNIT)) {
            runCatching { mailSender.sendBcc(addresses, mailMessage.subject, body, files) }
                .onSuccess { succeeded.addAll(addresses) }
                .onFailure { failed.addAll(addresses) }
        }

        saveMailHistories(mailMessage.id, succeeded, failed)
    }

    fun generateMailBody(mailData: MailData): String {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "content" to markdownToEmbeddedHtml(mailData.body),
                    "url" to applicationProperties.url
                )
            )
        }
        return templateEngine.process("mail/common", context)
    }

    private fun saveMailHistories(
        mailMessageId: Long,
        succeeded: MutableList<String>,
        failed: MutableList<String>
    ) {
        val mailHistories = mutableListOf<MailHistory>()

        if (succeeded.isNotEmpty()) {
            mailHistories.add(MailHistory(mailMessageId, succeeded, true))
        }

        if (failed.isNotEmpty()) {
            mailHistories.add(MailHistory(mailMessageId, failed, false))
        }

        mailHistoryRepository.saveAll(mailHistories)
    }
}
