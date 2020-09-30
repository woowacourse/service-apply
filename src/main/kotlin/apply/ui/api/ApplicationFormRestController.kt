package apply.ui.api

import apply.application.SaveApplicationFormRequest
import apply.application.ApplicationFormService
import apply.application.UpdateApplicationFormRequest
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
    fun getForm(@RequestParam("recruitmentId") recruitment: Long): ResponseEntity<Any> {
        return try {
            val form = applicationFormService.findForm(1L, recruitment)
            ResponseEntity.ok().body(form)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping
    fun save(
        @RequestBody saveApplicationFormRequest: SaveApplicationFormRequest
    ): ResponseEntity<String> {
        return try {
            applicationFormService.save(1L, saveApplicationFormRequest)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
    // TODO: 20. 9. 22. ControllerAdvice로 리팩토링

    @PutMapping
    fun update(
        @RequestBody updateApplicationFormRequest: UpdateApplicationFormRequest
    ): ResponseEntity<String> {
        return try {
            applicationFormService.update(1L, updateApplicationFormRequest)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
