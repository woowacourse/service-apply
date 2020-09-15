package apply.ui.api

import apply.application.ApplicantService
import apply.domain.applicant.dto.ApplicantRequest
import apply.domain.applicant.exception.ApplicantValidateException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applicants")
class ApplicantRestController(
    private val applicantService: ApplicantService
) {
    @PostMapping("/{recruitmentId}")
    fun generateToken(
        @PathVariable recruitmentId: Long,
        @RequestBody applicantRequest: ApplicantRequest
    ): ResponseEntity<String> {
        return try {
            val token = applicantService.validateOrCreateApplicantAndGenerateToken(recruitmentId, applicantRequest)
            ResponseEntity.ok().body(token)
        } catch (e: ApplicantValidateException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        }
    }
}
