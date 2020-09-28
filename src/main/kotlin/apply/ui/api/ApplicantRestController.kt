package apply.ui.api

import apply.application.ApplicantService
import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantVerifyInformation
import apply.domain.applicant.exception.ApplicantValidateException
import org.springframework.http.HttpStatus
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
    @PostMapping("/register")
    fun generateToken(@RequestBody applicantInformation: ApplicantInformation): ResponseEntity<String> {
        return try {
            val token = applicantService.generateToken(applicantInformation)
            ResponseEntity.ok().body(token)
        } catch (e: ApplicantValidateException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        }
    }

    @PostMapping("/login")
    fun generateToken(@RequestBody applicantVerifyInformation: ApplicantVerifyInformation): ResponseEntity<String> {
        return try {
            val token = applicantService.generateTokenByLogin(applicantVerifyInformation)
            ResponseEntity.ok().body(token)
        } catch (e: ApplicantValidateException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
        }
    }
}
