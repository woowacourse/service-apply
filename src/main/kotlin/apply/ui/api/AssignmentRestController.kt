package apply.ui.api

import apply.application.AssignmentService
import apply.application.CreateAssignmentRequest
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/missions/{missionId}/assignments")
class AssignmentRestController(
    private val assignmentService: AssignmentService
) {
    @PostMapping
    fun createAssignment(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @PathVariable missionId: Long,
        @RequestBody @Valid assignmentData: CreateAssignmentRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        assignmentService.create(missionId, applicant.id, assignmentData)
        return ResponseEntity.ok().build()
    }
}
