package apply.ui.api

import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.application.AssignmentService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}/missions/{missionId}/assignments")
class AssignmentRestController(
    private val assignmentService: AssignmentService
) {
    @PostMapping
    fun create(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: AssignmentRequest,
        @LoginUser user: User
    ): ResponseEntity<Unit> {
        assignmentService.create(missionId, user.id, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping
    fun getAssignmentByUserIdAndMissionId(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<AssignmentResponse>> {
        return ResponseEntity.ok(ApiResponse.success(
            assignmentService.getAssignmentByUserIdAndMissionId(user.id, missionId))
        )
    }

    @PatchMapping
    fun update(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: AssignmentRequest,
        @LoginUser user: User
    ): ResponseEntity<Unit> {
        assignmentService.update(missionId, user.id, request)
        return ResponseEntity.ok().build()
    }
}
