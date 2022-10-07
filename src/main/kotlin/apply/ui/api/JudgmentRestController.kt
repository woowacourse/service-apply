package apply.ui.api

import apply.application.FailJudgmentRequest
import apply.application.JudgmentService
import apply.application.LastJudgmentResponse
import apply.application.SuccessJudgmentRequest
import apply.domain.user.User
import apply.security.Accessor
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
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

    @PostMapping("/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-real")
    fun judgeReal(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.judgeReal(user.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @PostMapping("/judgments/{judgmentId}/success")
    fun success(
        @PathVariable judgmentId: Long,
        @RequestBody request: SuccessJudgmentRequest,
        @Accessor("lambda") ignored: Unit
    ): ResponseEntity<Void> {
        judgmentService.success(judgmentId, request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/judgments/{judgmentId}/fail")
    fun fail(
        @PathVariable judgmentId: Long,
        @RequestBody request: FailJudgmentRequest,
        @Accessor("lambda") ignored: Unit
    ): ResponseEntity<Void> {
        judgmentService.fail(judgmentId, request)
        return ResponseEntity.ok().build()
    }
}
