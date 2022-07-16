package apply.ui.api

import apply.application.TermResponse
import apply.application.TermData
import apply.application.TermService
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
        @RequestBody termData: TermData
    ): ResponseEntity<Unit> {
        val savedId = termService.save(termData)
        return ResponseEntity.created(URI.create("/api/terms/$savedId")).build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<TermResponse>>> {
        val terms = termService.findAll()
        return ResponseEntity.ok(ApiResponse.success(terms))
    }

    @DeleteMapping("/{termId}")
    fun deleteById(
        @PathVariable termId: Long
    ): ResponseEntity<Unit> {
        termService.deleteById(termId)
        return ResponseEntity.ok().build()
    }
}
