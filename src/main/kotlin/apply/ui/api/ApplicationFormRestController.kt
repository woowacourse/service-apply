package apply.ui.api

import apply.application.ApplicationFormService
import apply.application.SaveApplicationFormRequest
import apply.application.UpdateApplicationFormRequest
import apply.domain.applicant.Applicant
import apply.security.LoginApplicant
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/application-forms")
class ApplicationFormRestController(
    private val applicationFormService: ApplicationFormService
) {
    @GetMapping
    fun getForm(
        @RequestParam("recruitmentId") recruitment: Long,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<Any> {
        return try {
            val form = applicationFormService.findForm(applicant.id, recruitment)
            ResponseEntity.ok().body(form)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun save(
        @RequestBody saveApplicationFormRequest: SaveApplicationFormRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<String> {
        return try {
            applicationFormService.save(applicant.id, saveApplicationFormRequest)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
    // TODO: 20. 9. 22. ControllerAdvice로 리팩토링

    @PutMapping
    fun update(
        @RequestBody updateApplicationFormRequest: UpdateApplicationFormRequest,
        @LoginApplicant applicant: Applicant
    ): ResponseEntity<String> {
        return try {
            applicationFormService.update(applicant.id, updateApplicationFormRequest)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
