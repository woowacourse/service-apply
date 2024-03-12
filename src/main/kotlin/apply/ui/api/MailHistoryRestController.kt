package apply.ui.api

import apply.application.MailHistoryService
import apply.application.mail.MailData
import apply.domain.member.Member
import apply.security.LoginMember
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
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<MailData>> {
        return ResponseEntity.ok(ApiResponse.success(mailHistoryService.getById(mailHistoryId)))
    }

    @GetMapping
    fun findAll(
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<List<MailData>>> {
        return ResponseEntity.ok(ApiResponse.success(mailHistoryService.findAll()))
    }
}
