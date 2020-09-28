package apply.ui.api

import apply.application.ApplicationFormSaveRequest
import apply.application.ApplicationFormService
import apply.application.ApplicationFormUpdateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applications")
class ApplicationFormRestController(
    private val applicationFormService: ApplicationFormService
) {
    @GetMapping("")
    fun getApplicationForm(@RequestParam("recruitmentId") recruitment: Long): ResponseEntity<Any> {
        return try {
            val form = applicationFormService.getForm(1L, recruitment)
            ResponseEntity.ok().body(form)
        } catch (e: NoSuchElementException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("")
    fun saveApplicationForm(
        @RequestBody applicationFormSaveRequest: ApplicationFormSaveRequest
    ): ResponseEntity<String> {
        return try {
            applicationFormService.save(1L, applicationFormSaveRequest)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
    // TODO: 20. 9. 22. ControllerAdvice로 리팩토링

    @PutMapping("")
    fun updateApplicationForm(
        @RequestBody applicationFormUpdateRequest: ApplicationFormUpdateRequest
    ): ResponseEntity<String> {
        return try {
            applicationFormService.update(1L, applicationFormUpdateRequest)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
