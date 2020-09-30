package apply.application

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

private const val SUBJECT: String = "임시 비밀번호 입니다"

@Service
class MailService(
    private val mailSender: JavaMailSender,
    private val message: SimpleMailMessage
) {
    fun sendPasswordResetMail(receiveAddress: String, newPassword: String) {
        message.setTo(receiveAddress)
        message.setSubject(SUBJECT)
        message.setText(newPassword)

        mailSender.send(message)
    }
}
