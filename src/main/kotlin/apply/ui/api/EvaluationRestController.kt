package apply.ui.api

import apply.application.EvaluationData
import apply.application.EvaluationGridResponse
import apply.application.EvaluationResponse
import apply.application.EvaluationService
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

@RequestMapping("/api/recruitments/{recruitmentId}/evaluations")
@RestController
class EvaluationRestController(
    private val evaluationService: EvaluationService
) {
    @PostMapping
    fun save(
        @PathVariable recruitmentId: Long,
        @RequestBody evaluationData: EvaluationData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<EvaluationResponse>> {
        val response = evaluationService.save(evaluationData)
        return ResponseEntity.created("/api/recruitments/$recruitmentId/evaluations/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping("/{evaluationId}")
    fun getById(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<EvaluationResponse>> {
        val response = evaluationService.getById(evaluationId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping
    fun findAllWithRecruitment(
        @PathVariable recruitmentId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<EvaluationGridResponse>>> {
        val responses = evaluationService.findAllWithRecruitment()
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @DeleteMapping("/{evaluationId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        evaluationService.deleteById(evaluationId)
        return ResponseEntity.ok().build()
    }
}
