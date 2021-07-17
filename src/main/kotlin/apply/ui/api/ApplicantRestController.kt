package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantAuthenticationService
import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.AuthenticateApplicantRequest
import apply.application.EditPasswordRequest
import apply.application.RegisterApplicantRequest
import apply.application.ResetPasswordRequest
import apply.application.mail.MailService
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ApplicantRestController(
    private val applicantService: ApplicantService,
    private val applicantAuthenticationService: ApplicantAuthenticationService,
    private val mailService: MailService
) {
    @PostMapping("/applicants/register")
    fun generateToken(@RequestBody @Valid request: RegisterApplicantRequest): ResponseEntity<ApiResponse<String>> {
        val token = applicantAuthenticationService.generateToken(request)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/applicants/login")
    fun generateToken(@RequestBody @Valid request: AuthenticateApplicantRequest): ResponseEntity<ApiResponse<String>> {
        val token = applicantAuthenticationService.generateTokenByLogin(request)
        return ResponseEntity.ok().body(ApiResponse.success(token))
    }

    @PostMapping("/applicants/reset-password")
    fun resetPassword(@RequestBody @Valid request: ResetPasswordRequest): ResponseEntity<Unit> {
        val newPassword = applicantService.resetPassword(request)
        mailService.sendPasswordResetMail(request, newPassword)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/applicants/edit-password")
    fun editPassword(
        @RequestBody @Valid request: EditPasswordRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicantService.editPassword(applicant.id, request)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/recruitments/{recruitmentId}/application-forms")
    fun findAllByRecruitmentIdAndKeyword(
        @PathVariable recruitmentId: Long,
        @RequestParam applicantKeyword: String?
    ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
        val applicants = applicantService.findAllByRecruitmentIdAndSubmittedTrueAndKeyword(recruitmentId, applicantKeyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }

    @GetMapping("/applicants")
    fun findAllByKeyword(
        @RequestParam keyword: String
    ): ResponseEntity<ApiResponse<List<ApplicantResponse>>> {
        val applicants = applicantService.findAllByKeyword(keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
