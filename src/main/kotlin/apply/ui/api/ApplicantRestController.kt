package apply.ui.api

import apply.application.RegisterApplicantRequest
import apply.application.ApplicantService
import apply.application.ApplicantAuthenticationService
import apply.application.EditPasswordRequest
import apply.application.RegisterApplicantRequest
import apply.application.ResetPasswordRequest
import apply.application.mail.MailService
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/applicants")
class ApplicantRestController(
    private val applicantService: ApplicantService,
    private val applicantAuthenticationService: ApplicantAuthenticationService,
    private val mailService: MailService,
    private val applicationProperties: ApplicationProperties,
) {
    @PostMapping("/register")
    fun generateToken(@RequestBody @Valid request: RegisterApplicantRequest): ResponseEntity<ApiResponse<String>> {
        val token = applicantAuthenticationService.generateToken(request)
        val applicant = applicantService.getByEmail(request.email)
        mailService.sendAuthenticationMail(request, applicant.authenticateCode)
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

    @GetMapping("/authenticate-email")
    fun authenticateEmail(
        @RequestParam email: String,
        @RequestParam authenticateCode: String
    ): ResponseEntity<Unit> {
        applicantService.authenticateApplicant(email, authenticateCode)
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", applicationProperties.url).build()
    }

    @GetMapping
    fun findAllByKeyword(
        @RequestParam keyword: String
    ): ResponseEntity<ApiResponse<List<ApplicantResponse>>> {
        val applicants = applicantService.findAllByKeyword(keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
