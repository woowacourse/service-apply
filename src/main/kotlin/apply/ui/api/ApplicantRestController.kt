package apply.ui.api

import apply.application.*
import apply.application.mail.MailService
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
    private val applicantAuthenticationService: ApplicantAuthenticationService,
    private val mailService: MailService
) {
    @PostMapping("/register")
    fun generateToken(@RequestBody @Valid request: RegisterApplicantRequest): ResponseEntity<ApiResponse<String>> {
        val token = applicantAuthenticationService.generateToken(request)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/login")
    fun generateToken(@RequestBody @Valid request: AuthenticateApplicantRequest): ResponseEntity<ApiResponse<String>> {
        val token = applicantAuthenticationService.generateTokenByLogin(request)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody @Valid request: ResetPasswordRequest): ResponseEntity<Unit> {
        val newPassword = applicantService.resetPassword(request)
        mailService.sendPasswordResetMail(request, newPassword)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/edit-password")
    fun editPassword(
        @RequestBody @Valid request: EditPasswordRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicantService.editPassword(applicant.id, request)
        return ResponseEntity.noContent().build()
    }
}
