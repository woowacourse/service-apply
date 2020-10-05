package apply.application

import apply.domain.applicant.ApplicantPasswordFindInformation
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
    fun sendPasswordResetMail(applicantPasswordFindInformation: ApplicantPasswordFindInformation, newPassword: String) {
        message.setTo(applicantPasswordFindInformation.email)
        message.setSubject("${applicantPasswordFindInformation.name}님, 임시 비밀번호를 발송해 드립니다.")
        message.setText(newPassword)

        mailSender.send(message)
    }
}
