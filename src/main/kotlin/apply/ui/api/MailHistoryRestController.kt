package apply.ui.api

import apply.application.MailHistoryService
import apply.application.mail.MailData
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/mail-history")
class MailHistoryRestController(
    private val mailHistoryService: MailHistoryService
) {
    @PostMapping
    fun save(
        @RequestBody @Valid request: MailData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        // todo: 파일 첨부하여 보내는 로직 필요
        val savedId = mailHistoryService.save(request)
        return ResponseEntity.created(URI.create("/api/mail-history/$savedId")).build()
    }

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
