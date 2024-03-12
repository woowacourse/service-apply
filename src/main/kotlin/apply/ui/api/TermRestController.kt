package apply.ui.api

import apply.application.TermData
import apply.application.TermResponse
import apply.application.TermService
import apply.domain.member.Member
import apply.security.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri

@RequestMapping("/api/terms")
@RestController
class TermRestController(
    private val termService: TermService
) {
    @PostMapping
    fun save(
        @RequestBody termData: TermData,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<TermResponse>> {
        val response = termService.save(termData)
        return ResponseEntity.created("/api/terms/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping("/{termId}")
    fun getById(
        @PathVariable termId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<TermResponse>> {
        val response = termService.getById(termId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping
    fun findAll(
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<List<TermResponse>>> {
        val responses = termService.findAll()
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @DeleteMapping("/{termId}")
    fun deleteById(
        @PathVariable termId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<Unit> {
        termService.deleteById(termId)
        return ResponseEntity.ok().build()
    }
}
