package apply.ui.api

import apply.application.EvaluationData
import apply.application.EvaluationResponse
import apply.application.EvaluationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}/evaluations")
class EvaluationRestController(
    private val evaluationService: EvaluationService,
) {
    @PostMapping
    fun createEvaluation(
        @PathVariable recruitmentId: Long,
        @RequestBody evaluationData: EvaluationData,
    ): ResponseEntity<Unit> {
        evaluationService.save(evaluationData)
        return ResponseEntity.ok().body(Unit)
    }

    @GetMapping("/{evaluationId}")
    fun getDataById(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
    ): ResponseEntity<ApiResponse<EvaluationData>> {
        val evaluationData = evaluationService.getDataById(evaluationId)
        return ResponseEntity.ok(ApiResponse.success(evaluationData))
    }

    @GetMapping
    fun findAllWithRecruitment(@PathVariable recruitmentId: Long): ResponseEntity<ApiResponse<List<EvaluationResponse>>> {
        val evaluationResponses = evaluationService.findAllWithRecruitment()
        return ResponseEntity.ok(ApiResponse.success(evaluationResponses))
    }

    @DeleteMapping("/{evaluationId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
    ): ResponseEntity<Unit> {
        evaluationService.deleteById(evaluationId)
        return ResponseEntity.ok().body(Unit)
    }
}
