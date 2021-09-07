package apply.ui.api

import apply.application.EvaluationSendingTargetRequest
import apply.application.EvaluationSendingTargetResponse
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets")
class EvaluationTargetRestController(
    private val evaluationTargetService: EvaluationTargetService
) {
    @GetMapping
    fun findAllByEvaluationIdAndKeyword(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestParam keyword: String
    ): ResponseEntity<ApiResponse<List<EvaluationTargetResponse>>> {
        val evaluationTargets =
            evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword)
        return ResponseEntity.ok(ApiResponse.success(evaluationTargets))
    }

    @PutMapping("/renew")
    fun load(@PathVariable recruitmentId: Long, @PathVariable evaluationId: Long): ResponseEntity<Unit> {
        evaluationTargetService.load(evaluationId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{targetId}/grade")
    fun getGradeEvaluation(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @PathVariable targetId: Long
    ): ResponseEntity<ApiResponse<GradeEvaluationResponse>> {
        val gradeEvaluation = evaluationTargetService.getGradeEvaluation(targetId)
        return ResponseEntity.ok(ApiResponse.success(gradeEvaluation))
    }

    @PatchMapping("/{targetId}/grade")
    fun grade(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @PathVariable targetId: Long,
        @RequestBody request: EvaluationTargetData
    ): ResponseEntity<Unit> {
        evaluationTargetService.grade(targetId, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/emails")
    fun findAllMailSendingTargetEmail(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestBody request: EvaluationSendingTargetRequest,
    ): ResponseEntity<ApiResponse<List<EvaluationSendingTargetResponse>>> {
        val mailSendingTargetEmails = evaluationTargetService.findAllMailSendingTargetEmail(evaluationId, request)
        return ResponseEntity.ok(ApiResponse.success(mailSendingTargetEmails))
    }
}
