package apply.ui.api

import apply.application.MailSendingTargetResponse
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import apply.application.MailSendingService
import apply.domain.evaluationtarget.EvaluationStatus
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
    private val evaluationTargetService: EvaluationTargetService,
    private val mailSendingService: MailSendingService,
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
    fun findAllMailSendingTargets(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestParam status: EvaluationStatus?,
    ): ResponseEntity<ApiResponse<List<MailSendingTargetResponse>>> {
        val mailSendingTargets = mailSendingService.findMailSendingTargetsByEvaluationStatus(evaluationId, status)
        return ResponseEntity.ok(ApiResponse.success(mailSendingTargets))
    }
}
