package apply.ui.api

import apply.application.mail.MailData
import apply.application.mail.MailService
import apply.domain.user.User
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
    private val mailService: MailService
) {
    @PostMapping
    fun sendMail(
        @RequestPart request: MailData,
        @RequestPart files: Array<MultipartFile>,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        val inputStreamFiles = files.associate { (it.originalFilename!! to ByteArrayResource(it.bytes)) }
        mailService.sendMailsByBcc(request, inputStreamFiles)
        return ResponseEntity.noContent().build()
    }
}
