package apply.ui.api

import apply.application.mail.MailReservationService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/mail-reservation")
@RestController
class MailReservationRestController(
    private val mailReservationService: MailReservationService
) {
    @PostMapping
    fun sendMail(
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        mailReservationService.sendMail()
        return ResponseEntity.noContent().build()
    }
}
