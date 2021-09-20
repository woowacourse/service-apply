package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.MyApplicationFormResponse
import apply.application.UpdateApplicationFormRequest
import apply.application.mail.MailService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ApplicationFormRestController(
    private val applicationFormService: ApplicationFormService,
    private val applicantService: ApplicantService,
    private val mailService: MailService
) {
    @GetMapping("/application-forms/me")
    fun getMyApplicationForms(
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<List<MyApplicationFormResponse>>> {
        val applicant = applicantService.getByEmail(user.email)
        val form = applicationFormService.getMyApplicationForms(applicant.id)
        return ResponseEntity.ok().body(ApiResponse.success(form))
    }

    @GetMapping("/application-forms")
    fun getForm(
        @RequestParam recruitmentId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<ApplicationFormResponse>> {
        val applicant = applicantService.getByEmail(user.email)
        val form = applicationFormService.getApplicationForm(applicant.id, recruitmentId)
        return ResponseEntity.ok().body(ApiResponse.success(form))
    }

    @PostMapping("/application-forms")
    fun create(
        @RequestBody @Valid request: CreateApplicationFormRequest,
        @LoginUser user: User
    ): ResponseEntity<Unit> {
        val applicant = applicantService.getByEmail(user.email)
        applicationFormService.create(applicant.id, request)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/application-forms")
    fun update(
        @RequestBody @Valid request: UpdateApplicationFormRequest,
        @LoginUser user: User
    ): ResponseEntity<Unit> {
        val applicant = applicantService.getByEmail(user.email)
        applicationFormService.update(applicant.id, request)

        // TODO: 차후 연결될 프론트 주소가 필요
        mailService.sendFormSubmittedMail(applicant)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/recruitments/{recruitmentId}/application-forms")
    fun findAllByRecruitmentIdAndKeyword(
        @PathVariable recruitmentId: Long,
        @RequestParam keyword: String?
    ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
        val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
