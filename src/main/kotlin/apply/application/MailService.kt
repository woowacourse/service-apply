package apply.application

import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: MailSender,
    private val mailProperties: MailProperties
) {
    @Async
    fun sendPasswordResetMail(resetPasswordRequest: ResetPasswordRequest, newPassword: String) {
        val message = SimpleMailMessage()
        message.setFrom(mailProperties.username)
        message.setTo(resetPasswordRequest.email)
        message.setSubject("${resetPasswordRequest.name}님, 임시 비밀번호를 발송해 드립니다.")
        message.setText(newPassword)

        mailSender.send(message)
    }
}
