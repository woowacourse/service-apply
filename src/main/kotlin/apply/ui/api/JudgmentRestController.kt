package apply.ui.api

import apply.application.JudgmentService
import apply.application.LastJudgmentResponse
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/recruitments/{recruitmentId}/missions/{missionId}")
@RestController
class JudgmentRestController(
    private val judgmentService: JudgmentService
) {
    @PostMapping("/judgments/judge-example")
    fun judgeExample(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<LastJudgmentResponse>> {
        val response = judgmentService.judgeExample(user.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
