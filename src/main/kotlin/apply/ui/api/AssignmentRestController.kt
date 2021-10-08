package apply.ui.api

import apply.application.AssignmentService
import apply.application.CreateAssignmentRequest
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
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
    fun createAssignment(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid request: CreateAssignmentRequest,
        @LoginUser applicant: User
    ): ResponseEntity<Unit> {
        assignmentService.create(missionId, applicant.id, request)
        return ResponseEntity.ok().build()
    }
}
