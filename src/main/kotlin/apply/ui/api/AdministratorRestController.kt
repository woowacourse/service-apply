package apply.ui.api

import apply.application.AdministratorData
import apply.application.AdministratorResponse
import apply.application.AdministratorService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri
import javax.validation.Valid

@RequestMapping("/api/administrators")
@RestController
class AdministratorRestController(
    private val administratorService: AdministratorService
) {
    @PostMapping
    fun save(
        @RequestBody @Valid request: AdministratorData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<AdministratorResponse>> {
        val response = administratorService.save(request)
        return ResponseEntity.created("/api/administrators/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping
    fun findAll(
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<AdministratorResponse>>> {
        val response = administratorService.findAll()
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/{administratorId}")
    fun findById(
        @PathVariable administratorId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<AdministratorResponse>> {
        val response = administratorService.findById(administratorId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @DeleteMapping("/{administratorId}")
    fun deleteById(
        @PathVariable administratorId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        administratorService.deleteById(administratorId)
        return ResponseEntity.noContent().build()
    }
}
