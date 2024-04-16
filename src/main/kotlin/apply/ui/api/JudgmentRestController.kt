package apply.ui.api

import apply.application.CancelJudgmentRequest
import apply.application.FailJudgmentRequest
import apply.application.JudgmentAllService
import apply.application.JudgmentService
import apply.application.LastJudgmentResponse
import apply.application.SuccessJudgmentRequest
import apply.domain.member.Member
import apply.security.Accessor
import apply.security.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class JudgmentRestController(
    private val judgmentService: JudgmentService,
    private val judgmentAllService: JudgmentAllService
) {
    @PostMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-example")
    fun judgeExample(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginMember member: Member
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.judgeExample(member.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-example")
    fun findExample(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginMember member: Member
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.findLastExampleJudgment(member.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/judgments/{judgmentId}/success")
    fun success(
        @PathVariable judgmentId: Long,
        @RequestBody request: SuccessJudgmentRequest,
        @Accessor("lambda") ignored: Unit
    ): ResponseEntity<Unit> {
        judgmentService.success(judgmentId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/judgments/{judgmentId}/fail")
    fun fail(
        @PathVariable judgmentId: Long,
        @RequestBody request: FailJudgmentRequest,
        @Accessor("lambda") ignored: Unit
    ): ResponseEntity<Unit> {
        judgmentService.fail(judgmentId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/judgments/{judgmentId}/cancel")
    fun cancel(
        @PathVariable judgmentId: Long,
        @RequestBody request: CancelJudgmentRequest,
        @Accessor("lambda") ignored: Unit
    ): ResponseEntity<Unit> {
        judgmentService.cancel(judgmentId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/evaluations/{evaluationId}/assignments/{assignmentId}/judgments/judge-real")
    fun judgeReal(
        @PathVariable evaluationId: Long,
        @PathVariable assignmentId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.judgeReal(assignmentId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/evaluations/{evaluationId}/judgments/judge-all")
    fun judgeAll(
        @PathVariable evaluationId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<Unit> {
        judgmentAllService.judgeAll(evaluationId)
        return ResponseEntity.ok().build()
    }
}
