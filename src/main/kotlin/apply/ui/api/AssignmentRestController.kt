package apply.ui.api

import apply.application.AssignmentData
import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.application.AssignmentService
import apply.domain.member.Member
import apply.security.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri
import javax.validation.Valid

@RequestMapping("/api/recruitments/{recruitmentId}")
@RestController
class AssignmentRestController(
    private val assignmentService: AssignmentService
) {
    @PostMapping("/missions/{missionId}/assignments")
    fun create(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: AssignmentRequest,
        @LoginMember member: Member
    ): ResponseEntity<ApiResponse<AssignmentResponse>> {
        val response = assignmentService.create(missionId, member.id, request)
        return ResponseEntity.created("/missions/${response.id}/assignments/me".toUri())
            .body(ApiResponse.success(response))
    }

    @PatchMapping("/missions/{missionId}/assignments")
    fun update(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: AssignmentRequest,
        @LoginMember member: Member
    ): ResponseEntity<Unit> {
        assignmentService.update(missionId, member.id, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/missions/{missionId}/assignments/me")
    fun getAssignment(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginMember member: Member
    ): ResponseEntity<ApiResponse<AssignmentResponse>> {
        val assignment = assignmentService.getByMemberIdAndMissionId(member.id, missionId)
        return ResponseEntity.ok(ApiResponse.success(assignment))
    }

    @GetMapping("/targets/{targetId}/assignments")
    fun findByEvaluationTargetId(
        @PathVariable recruitmentId: Long,
        @PathVariable targetId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<AssignmentData>> {
        val assignments = assignmentService.findByEvaluationTargetId(targetId)
        return ResponseEntity.ok(ApiResponse.success(assignments))
    }
}
