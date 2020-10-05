package apply.application

import apply.domain.applicant.ResetPasswordRequest
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: MailSender,
    private val message: SimpleMailMessage
) {
    @Async
    fun sendPasswordResetMail(resetPasswordRequest: ResetPasswordRequest, newPassword: String) {
        message.setTo(resetPasswordRequest.email)
        message.setSubject("${resetPasswordRequest.name}님, 임시 비밀번호를 발송해 드립니다.")
        message.setText(newPassword)

        mailSender.send(message)
    }
}
