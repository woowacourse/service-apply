package apply.ui.api

import apply.application.ApplicationFormSaveRequest
import apply.application.ApplicationFormService
import apply.application.ApplicationFormUpdateRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/register/application")
class ApplicationFormController(
    private val applicationFormService: ApplicationFormService
) {
    @GetMapping("/{id}")
    fun getApplicationForm(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val form = applicationFormService.getForm(1L, id)
            ResponseEntity.ok().body(form)
        } catch (e: NoSuchElementException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/{id}")
    fun saveApplicationForm(
        @PathVariable id: Long,
        @RequestBody applicationFormSaveRequest: ApplicationFormSaveRequest
    ): ResponseEntity<String> {
        return try {
            applicationFormService.save(1L, id, applicationFormSaveRequest)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
    // TODO: 20. 9. 22. ControllerAdvice로 리팩토링

    @PutMapping("/{id}")
    fun updateApplicationForm(
        @PathVariable id: Long,
        @RequestBody applicationFormUpdateRequest: ApplicationFormUpdateRequest
    ): ResponseEntity<String> {
        return try {
            applicationFormService.update(1L, id, applicationFormUpdateRequest)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
