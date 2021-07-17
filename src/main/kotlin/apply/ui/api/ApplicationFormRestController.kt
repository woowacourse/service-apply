package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.MyApplicationFormResponse
import apply.application.UpdateApplicationFormRequest
import apply.application.mail.MailService
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
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
@RequestMapping("/api/recruitments/{recruitmentId}/application-forms")
class ApplicationFormRestController(
    private val applicationFormService: ApplicationFormService,
    private val applicantService: ApplicantService,
    private val mailService: MailService
) {
    @GetMapping("/me")
    fun getMyApplicationForms(@PathVariable recruitmentId: Long, @LoginApplicant applicant: Applicant): ResponseEntity<ApiResponse<List<MyApplicationFormResponse>>> {
        val form = applicationFormService.getMyApplicationForms(applicant.id)
        return ResponseEntity.ok().body(ApiResponse.success(form))
    }

    @GetMapping
    fun getForm(
        @PathVariable recruitmentId: Long,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<ApiResponse<ApplicationFormResponse>> {
        val form = applicationFormService.getApplicationForm(applicant.id, recruitmentId)
        return ResponseEntity.ok().body(ApiResponse.success(form))
    }

    @PostMapping
    fun create(
        @PathVariable recruitmentId: Long,
        @RequestBody @Valid request: CreateApplicationFormRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicationFormService.create(applicant.id, request)
        return ResponseEntity.ok().build()
    }

    @PatchMapping
    fun update(
        @PathVariable recruitmentId: Long,
        @RequestBody @Valid request: UpdateApplicationFormRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicationFormService.update(applicant.id, request)
        mailService.sendFormSubmittedMail(applicant)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/applicants")
    fun findAllByRecruitmentIdAndKeyword(
        @PathVariable recruitmentId: Long,
        @RequestParam keyword: String?
    ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
        val applicants = applicantService.findAllByRecruitmentIdAndSubmittedTrueAndKeyword(recruitmentId, keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
