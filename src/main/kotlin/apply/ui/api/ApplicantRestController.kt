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

    // todo: jason에게 검토받기
    // @GetMapping("/{keyword}/recruitments/{recruitmentId}")
    // fun findAllByRecruitmentIdAndKeyword(
    //     @RequestParam(value = "recruitmentId") recruitmentId: Long,
    //     @RequestParam(value = "keyword") keyword: String?
    // ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
    //     if (keyword != null) {
    //         val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)
    //         return ResponseEntity.ok(ApiResponse.success(applicants))
    //     }
    //
    //     val applicants = applicantService.findAllByRecruitmentIdAndSubmittedTrue(recruitmentId)
    //     return ResponseEntity.ok(ApiResponse.success(applicants))
    // }

    @GetMapping("/{keyword}/recruitments/{recruitmentId}")
    fun findAllByRecruitmentIdAndKeyword(
        @PathVariable(value = "keyword") keyword: String,
        @PathVariable(value = "recruitmentId") recruitmentId: Long
    ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
        // 지원서 아이디로 지원자를 찾아 근데 지원자 키워드는 이메일이나 이름임// 지원자 이름이나 이메일과 지원서 아이디로 지원자를 찾는다.
        val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }

    @GetMapping("/recruitments/{recruitmentId}")
    fun findAllByRecruitmentIdAndSubmittedTrue(
        @PathVariable("recruitmentId", required = true) recruitmentId: Long
    ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
        val applicants = applicantService.findAllByRecruitmentIdAndSubmittedTrue(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }

    @GetMapping("/{keyword}")
    fun findAllByKeyword(
        @PathVariable(value = "keyword", required = true) keyword: String
    ): ResponseEntity<ApiResponse<List<ApplicantResponse>>> {
        val applicants = applicantService.findAllByKeyword(keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
