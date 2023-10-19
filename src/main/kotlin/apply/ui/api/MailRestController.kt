package apply.ui.api

import apply.application.mail.MailData
import apply.application.mail.MailMessageService
import apply.application.mail.SendingMailService
import apply.domain.user.User
import apply.security.Accessor
import apply.security.LoginUser
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/mail")
class MailRestController(
    private val sendingMailService: SendingMailService,
    private val mailMessageService: MailMessageService
) {
    @PostMapping
    fun sendMail(
        @RequestPart request: MailData,
        @RequestPart files: Array<MultipartFile>,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        val inputStreamFiles = files.associate { (it.originalFilename!! to ByteArrayResource(it.bytes)) }
        sendingMailService.sendMailByBcc(request, inputStreamFiles)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/reserved")
    fun sendMail(
        @Accessor("mail-scheduler") ignored: Unit
    ): ResponseEntity<Unit> {
        mailMessageService.sendReservedMails()
        return ResponseEntity.noContent().build()
    }
}
