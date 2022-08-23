package apply.ui.api

import apply.application.AdministratorData
import apply.application.AdministratorResponse
import apply.application.AdministratorService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri

@RequestMapping("/api/admin/administrators")
@RestController
class AdministratorRestController(
    private val administratorService: AdministratorService
) {

    @PostMapping
    fun save(
        @RequestBody request: AdministratorData,
        @LoginUser(administrator = true) user: User
    ) : ResponseEntity<ApiResponse<AdministratorResponse>> {
        val response = administratorService.save(request)
        return ResponseEntity.created("/api/admin/administrators/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }
}
