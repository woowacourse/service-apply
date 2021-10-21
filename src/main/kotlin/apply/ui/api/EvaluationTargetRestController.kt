package apply.ui.api

import apply.application.EvaluationTargetCsvService
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import apply.application.MailTargetResponse
import apply.application.MailTargetService
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets")
class EvaluationTargetRestController(
    private val evaluationTargetService: EvaluationTargetService,
    private val mailTargetService: MailTargetService,
    private val evaluationTargetCsvService: EvaluationTargetCsvService
) {
    @GetMapping
    fun findAllByEvaluationIdAndKeyword(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestParam keyword: String,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<EvaluationTargetResponse>>> {
        val evaluationTargets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword)
        return ResponseEntity.ok(ApiResponse.success(evaluationTargets))
    }

    @PutMapping("/renew")
    fun load(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        evaluationTargetService.load(evaluationId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{targetId}/grade")
    fun getGradeEvaluation(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @PathVariable targetId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<GradeEvaluationResponse>> {
        val gradeEvaluation = evaluationTargetService.getGradeEvaluation(targetId)
        return ResponseEntity.ok(ApiResponse.success(gradeEvaluation))
    }

    @PatchMapping("/{targetId}/grade")
    fun grade(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @PathVariable targetId: Long,
        @RequestBody request: EvaluationTargetData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        evaluationTargetService.grade(targetId, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/emails")
    fun findMailTargets(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestParam status: EvaluationStatus?,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<MailTargetResponse>>> {
        val mailSendingTargets = mailTargetService.findMailTargets(evaluationId, status)
        return ResponseEntity.ok(ApiResponse.success(mailSendingTargets))
    }

    @PatchMapping("/grade")
    fun gradeByCsv(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestParam file: MultipartFile,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        evaluationTargetCsvService.updateTarget(file.inputStream, evaluationId)
        return ResponseEntity.ok().build()
    }
}
