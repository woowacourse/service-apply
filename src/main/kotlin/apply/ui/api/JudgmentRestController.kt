package apply.ui.api

import apply.application.CancelJudgmentRequest
import apply.application.FailJudgmentRequest
import apply.application.JudgmentService
import apply.application.LastJudgmentResponse
import apply.application.SuccessJudgmentRequest
import apply.domain.user.User
import apply.security.Accessor
import apply.security.LoginUser
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
    private val judgmentService: JudgmentService
) {
    @PostMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-example")
    fun judgeExample(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.judgeExample(user.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-example")
    fun findExample(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.findExample(user.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-real")
    fun judgeReal(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.judgeReal(user.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-all")
    fun judgeAll(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        judgmentService.judgeAll(missionId)
        return ResponseEntity.ok().build()
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
}
