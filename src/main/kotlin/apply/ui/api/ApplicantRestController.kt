package apply.ui.api

import apply.application.ApplicantInformation
import apply.application.ApplicantService
import apply.application.ApplicantVerifyInformation
import apply.application.EditPasswordRequest
import apply.application.MailService
import apply.application.ResetPasswordRequest
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/applicants")
class ApplicantRestController(
    private val applicantService: ApplicantService,
    private val mailService: MailService
) {
    @PostMapping("/register")
    fun generateToken(@RequestBody @Valid applicantInformation: ApplicantInformation): ResponseEntity<ApiResponse<String>> {
        val token = applicantService.generateToken(applicantInformation)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/login")
    fun generateToken(@RequestBody @Valid applicantVerifyInformation: ApplicantVerifyInformation): ResponseEntity<ApiResponse<String>> {
        val token = applicantService.generateTokenByLogin(applicantVerifyInformation)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody @Valid resetPasswordRequest: ResetPasswordRequest): ResponseEntity<Unit> {
        val newPassword = applicantService.resetPassword(resetPasswordRequest)
        mailService.sendPasswordResetMail(resetPasswordRequest, newPassword)

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/edit-password")
    fun editPassword(
        @RequestBody @Valid editPasswordRequest: EditPasswordRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicantService.editPassword(applicant, editPasswordRequest)

        return ResponseEntity.noContent().build()
    }
}
