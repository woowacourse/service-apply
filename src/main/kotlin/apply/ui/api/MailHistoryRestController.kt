package apply.ui.api

import apply.application.MailHistoryService
import apply.application.mail.MailData
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/mail-history")
@RestController
class MailHistoryRestController(
    private val mailHistoryService: MailHistoryService
) {
    @GetMapping("/{mailHistoryId}")
    fun getById(
        @PathVariable mailHistoryId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<MailData>> {
        return ResponseEntity.ok(ApiResponse.success(mailHistoryService.getById(mailHistoryId)))
    }

    @GetMapping
    fun findAll(
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<MailData>>> {
        return ResponseEntity.ok(ApiResponse.success(mailHistoryService.findAll()))
    }
}
