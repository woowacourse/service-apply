package apply.ui.api

import apply.application.CheaterResponse
import apply.application.CheaterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cheaters")
class CheaterRestController(
    private val cheaterService: CheaterService
) {
    @GetMapping
    fun findAll(): ResponseEntity<ApiResponse<List<CheaterResponse>>> {
        val cheaters = cheaterService.findAll()
        return ResponseEntity.ok(ApiResponse.success(cheaters))
    }

    @PostMapping
    fun save(@RequestParam("applicantId") applicantId: Long): ResponseEntity<Unit> {
        cheaterService.save(applicantId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{cheaterId}")
    fun deleteById(@PathVariable("cheaterId") cheaterId: Long): ResponseEntity<Unit> {
        cheaterService.deleteById(cheaterId)
        return ResponseEntity.ok().build()
    }
}
