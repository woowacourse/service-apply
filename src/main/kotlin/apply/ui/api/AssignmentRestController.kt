package apply.ui.api

import apply.application.AssignmentData
import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.application.AssignmentService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}")
class AssignmentRestController(
    private val assignmentService: AssignmentService
) {
    @PostMapping("/missions/{missionId}/assignments")
    fun create(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: AssignmentRequest,
        @LoginUser user: User
    ): ResponseEntity<Unit> {
        assignmentService.create(missionId, user.id, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/missions/{missionId}/assignments")
    fun getAssignment(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<AssignmentResponse>> {
        val assignment = assignmentService.getByUserIdAndMissionId(user.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(assignment))
    }

    @PatchMapping("/missions/{missionId}/assignments")
    fun update(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: AssignmentRequest,
        @LoginUser user: User
    ): ResponseEntity<Unit> {
        assignmentService.update(missionId, user.id, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/evaluations/{evaluationId}/missions/{missionId}/targets/{targetId}/assignments")
    fun findByEvaluationTargetId(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: String,
        @PathVariable missionId: Long,
        @PathVariable targetId: Long
    ): ResponseEntity<ApiResponse<AssignmentData>> {
        val assignments = assignmentService.findByEvaluationTargetId(targetId)
        return ResponseEntity.ok(ApiResponse.success(assignments))
    }
}
