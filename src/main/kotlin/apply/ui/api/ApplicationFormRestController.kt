package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.MyApplicationFormResponse
import apply.application.UpdateApplicationFormRequest
import apply.domain.member.Member
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
import support.toUri
import javax.validation.Valid

@RequestMapping("/api")
@RestController
class ApplicationFormRestController(
    private val applicationFormService: ApplicationFormService,
    private val applicantService: ApplicantService
) {
    @PostMapping("/application-forms")
    fun create(
        @RequestBody @Valid request: CreateApplicationFormRequest,
        @LoginUser user: Member
    ): ResponseEntity<ApiResponse<ApplicationFormResponse>> {
        val response = applicationFormService.create(user.id, request)
        return ResponseEntity.created("/api/application-forms?recruitmentId=${response.recruitmentId}".toUri())
            .body(ApiResponse.success(response))
    }

    @PatchMapping("/application-forms")
    fun update(
        @RequestBody @Valid request: UpdateApplicationFormRequest,
        @LoginUser user: Member
    ): ResponseEntity<Unit> {
        applicationFormService.update(user.id, request)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/application-forms/me")
    fun getMyApplicationForms(
        @LoginUser user: Member
    ): ResponseEntity<ApiResponse<List<MyApplicationFormResponse>>> {
        val form = applicationFormService.getMyApplicationForms(user.id)
        return ResponseEntity.ok(ApiResponse.success(form))
    }

    @GetMapping("/application-forms")
    fun getApplicationForm(
        @RequestParam recruitmentId: Long,
        @LoginUser user: Member
    ): ResponseEntity<ApiResponse<ApplicationFormResponse>> {
        val form = applicationFormService.getApplicationForm(user.id, recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(form))
    }

    @GetMapping("/recruitments/{recruitmentId}/application-forms")
    fun findAllByRecruitmentIdAndKeyword(
        @PathVariable recruitmentId: Long,
        @RequestParam keyword: String?,
        @LoginUser(administrator = true) user: Member
    ): ResponseEntity<ApiResponse<List<ApplicantAndFormResponse>>> {
        val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId, keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
