package apply.ui.api

import apply.application.MailHistoryService
import apply.application.mail.MailData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/mails")
class MailHistoryRestController(
    private val mailHistoryService: MailHistoryService
) {
    @PostMapping
    fun save(@RequestBody request: MailData): ResponseEntity<Unit> {
        // todo: 파일 첨부하여 보내는 로직 필요
        mailHistoryService.save(request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{mailHistoryId}")
    fun getById(@PathVariable mailHistoryId: Long): ResponseEntity<ApiResponse<MailData>> {
        return ResponseEntity.ok(ApiResponse.success(mailHistoryService.getById(mailHistoryId)))
    }

    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<MailData>>> {
        return ResponseEntity.ok(ApiResponse.success(mailHistoryService.findAll()))
    }
}
