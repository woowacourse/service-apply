package apply.ui.api

import apply.application.ApplicantService
import apply.domain.applicant.dto.ApplicantRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applicants")
class ApplicantRestController(
    private val applicantService: ApplicantService
) {
    @PostMapping
    fun generateToken(@RequestBody applicantRequest: ApplicantRequest): ResponseEntity<String> {
        val token = applicantService.validateOrCreateApplicantAndGenerateToken(applicantRequest)
        return ResponseEntity.ok().body(token)
    }
}
