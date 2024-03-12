package apply.ui.api

import apply.application.CheaterData
import apply.application.CheaterResponse
import apply.application.CheaterService
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

@RequestMapping("/api/cheaters")
@RestController
class CheaterRestController(
    private val cheaterService: CheaterService
) {
    @PostMapping
    fun save(
        @RequestBody request: CheaterData,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<CheaterResponse>> {
        val response = cheaterService.save(request)
        return ResponseEntity.created("/api/cheaters/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping("/{cheaterId}")
    fun getById(
        @PathVariable cheaterId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<CheaterResponse>> {
        val response = cheaterService.getById(cheaterId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping
    fun findAll(
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<List<CheaterResponse>>> {
        val responses = cheaterService.findAll()
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @DeleteMapping("/{cheaterId}")
    fun deleteById(
        @PathVariable cheaterId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<Unit> {
        cheaterService.deleteById(cheaterId)
        return ResponseEntity.ok().build()
    }
}
