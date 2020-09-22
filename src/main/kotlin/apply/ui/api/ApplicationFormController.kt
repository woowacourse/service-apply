package apply.ui.api

import apply.application.ApplicationFormRequest
import apply.application.ApplicationFormService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/register/application")
class ApplicationFormController(
        private val applicationFormService: ApplicationFormService
) {
    @PostMapping("/{id}")
    // TODO: 20. 9. 22. json 형태 응답 고려하기
    fun saveApplicationForm(@PathVariable id: Long, @RequestBody applicationFormRequest: ApplicationFormRequest): ResponseEntity<String> {
        return try {
            applicationFormService.saveOrUpdate(1L, id, applicationFormRequest)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}