package apply.ui.api

import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class EvaluationTargetController(
    private val evaluationTargetService: EvaluationTargetService
) {
    @GetMapping("/evaluations/{evaluationId}/targets/{keyword}")
    fun findAllByEvaluationIdAndKeyword(
        @PathVariable evaluationId: Long,
        @PathVariable keyword: String
    ): ResponseEntity<ApiResponse<List<EvaluationTargetResponse>>> {
        val evaluationTargets =
            evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword)
        return ResponseEntity.ok(ApiResponse.success(evaluationTargets))
    }

    @PostMapping("/evaluations/{evaluationId}/targets")
    fun load(@PathVariable evaluationId: Long): ResponseEntity<Unit> {
        evaluationTargetService.load(evaluationId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/evaluations/targets/{targetId}")
    fun getGradeEvaluation(@PathVariable targetId: Long): ResponseEntity<ApiResponse<GradeEvaluationResponse>> {
        val gradeEvaluation = evaluationTargetService.getGradeEvaluation(targetId)
        return ResponseEntity.ok(ApiResponse.success(gradeEvaluation))
    }

    @PatchMapping("/evaluations-targets/{evaluationTargetId}")
    fun grade(
        @PathVariable evaluationTargetId: Long,
        @RequestBody request: EvaluationTargetData
    ): ResponseEntity<Unit> {
        evaluationTargetService.grade(evaluationTargetId, request)
        return ResponseEntity.ok().build()
    }
}
