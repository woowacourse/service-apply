package apply.ui.api

import apply.application.ApplicantService
import apply.application.MailService
import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantVerifyInformation
import apply.domain.applicant.ResetPasswordRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applicants")
class ApplicantRestController(
    private val applicantService: ApplicantService,
    private val mailService: MailService
) {
    @PostMapping("/register")
    fun generateToken(@RequestBody applicantInformation: ApplicantInformation): ResponseEntity<APIResponse<String>> {
        val token = applicantService.generateToken(applicantInformation)
        return ResponseEntity.ok().body(APIResponse(body = token))
    }

    @PostMapping("/login")
    fun generateToken(@RequestBody applicantVerifyInformation: ApplicantVerifyInformation): ResponseEntity<APIResponse<String>> {
        val token = applicantService.generateTokenByLogin(applicantVerifyInformation)
        return ResponseEntity.ok().body(APIResponse(body = token))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody resetPasswordRequest: ResetPasswordRequest): ResponseEntity<APIResponse<String>> {
        val newPassword = applicantService.resetPassword(resetPasswordRequest)
        mailService.sendPasswordResetMail(resetPasswordRequest, newPassword)

        return ResponseEntity.noContent().build()
    }
}
