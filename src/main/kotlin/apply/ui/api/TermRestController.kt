package apply.ui.api

import apply.application.TermData
import apply.application.TermResponse
import apply.application.TermService
import apply.domain.term.Term
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
@RequestMapping("/api/terms")
class TermRestController(
    private val termService: TermService
) {
    @PostMapping
    fun create(
        @RequestBody termData: TermData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<TermResponse>> {
        val term = termService.save(termData)
        return ResponseEntity.created(URI.create("/api/terms/${term.id}")).body(ApiResponse.success(term))
    }

    @GetMapping("/{termId}")
    fun getById(
        @PathVariable termId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<Term>> {
        val term = termService.getById(termId)
        return ResponseEntity.ok(ApiResponse.success(term))
    }

    @GetMapping
    fun findAll(
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<TermResponse>>> {
        val terms = termService.findAll()
        return ResponseEntity.ok(ApiResponse.success(terms))
    }

    @DeleteMapping("/{termId}")
    fun deleteById(
        @PathVariable termId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        termService.deleteById(termId)
        return ResponseEntity.ok().build()
    }
}
