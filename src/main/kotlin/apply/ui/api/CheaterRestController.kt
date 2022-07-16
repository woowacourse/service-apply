package apply.ui.api

import apply.application.CheaterData
import apply.application.CheaterResponse
import apply.application.CheaterService
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
import java.net.URI

@RestController
@RequestMapping("/api/cheaters")
class CheaterRestController(
    private val cheaterService: CheaterService
) {
    @GetMapping
    fun findAll(
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<CheaterResponse>>> {
        val cheaters = cheaterService.findAll()
        return ResponseEntity.ok(ApiResponse.success(cheaters))
    }

    @PostMapping
    fun save(
        @RequestBody request: CheaterData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        val savedId = cheaterService.save(request)
        return ResponseEntity.created(URI.create("/api/cheaters/$savedId")).build()
    }

    @DeleteMapping("/{cheaterId}")
    fun deleteById(
        @PathVariable cheaterId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        cheaterService.deleteById(cheaterId)
        return ResponseEntity.ok().build()
    }
}
