package apply.ui.api

import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.UpdateApplicationFormRequest
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/application-forms")
class ApplicationFormRestController(
    private val applicationFormService: ApplicationFormService
) {
    @GetMapping
    fun getForm(
        @RequestParam("recruitmentId") recruitment: Long,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<ApiResponse<ApplicationFormResponse>> {
        val form = applicationFormService.findForm(applicant.id, recruitment)
        return ResponseEntity.ok().body(ApiResponse.success(form))
    }

    @PostMapping
    fun create(
        @RequestBody @Valid request: CreateApplicationFormRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicationFormService.create(applicant.id, request)
        return ResponseEntity.ok().build()
    }

    @PatchMapping
    fun update(
        @RequestBody @Valid request: UpdateApplicationFormRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Unit> {
        applicationFormService.update(applicant.id, request)
        return ResponseEntity.ok().build()
    }
}
