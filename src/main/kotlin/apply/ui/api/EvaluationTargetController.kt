package apply.ui.api

import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import apply.domain.evaluationtarget.EvaluationTarget
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluations")
class EvaluationTargetController(
    private val evaluationTargetService: EvaluationTargetService
) {
    @GetMapping("/targets/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<EvaluationTarget> {
        val evaluationTargets = evaluationTargetService.getById(id)
        return ResponseEntity.ok(evaluationTargets)
    }

    @GetMapping("/{evaluationId}/targets")
    fun findAllByEvaluationId(
        @PathVariable evaluationId: Long
    ): ResponseEntity<ApiResponse<List<EvaluationTarget>>> {
        val evaluationTargets = evaluationTargetService.findAllByEvaluationId(evaluationId)
        return ResponseEntity.ok(ApiResponse.success(evaluationTargets))
    }

    @GetMapping("/{evaluationId}/targets/{keyword}")
    fun findAllByEvaluationIdAndKeyword(
        @PathVariable evaluationId: Long,
        @PathVariable keyword: String
    ): ResponseEntity<ApiResponse<List<EvaluationTargetResponse>>> {
        val evaluationTargets =
            evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword)
        return ResponseEntity.ok(ApiResponse.success(evaluationTargets))
    }

    @PostMapping("{evaluationId}/targets")
    fun load(@PathVariable evaluationId: Long): ResponseEntity<Unit> {
        evaluationTargetService.load(evaluationId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/targets/{targetId}")
    fun getGradeEvaluation(@PathVariable targetId: Long): ResponseEntity<ApiResponse<GradeEvaluationResponse>> {
        val gradeEvaluation = evaluationTargetService.getGradeEvaluation(targetId)
        return ResponseEntity.ok(ApiResponse.success(gradeEvaluation))
    }

    @PatchMapping("-targets/{evaluationTargetId}")
    fun grade(
        @PathVariable evaluationTargetId: Long,
        @RequestBody request: EvaluationTargetData
    ): ResponseEntity<Unit> {
        evaluationTargetService.grade(evaluationTargetId, request)
        return ResponseEntity.ok().build()
    }
}
