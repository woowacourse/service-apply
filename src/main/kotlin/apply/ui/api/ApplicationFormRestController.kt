package apply.ui.api

import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.SaveApplicationFormRequest
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
    fun getForm(@RequestParam("recruitmentId") recruitment: Long): ResponseEntity<ApiResponse<ApplicationFormResponse>> {
        val form = applicationFormService.findForm(1L, recruitment)
        return ResponseEntity.ok().body(ApiResponse(body = form))
    }

    @PostMapping
    fun save(
        @RequestBody saveApplicationFormRequest: SaveApplicationFormRequest
    ): ResponseEntity<String> {
        applicationFormService.save(1L, saveApplicationFormRequest)
        return ResponseEntity.ok().build()
    }

    @PutMapping
    fun update(
        @RequestBody updateApplicationFormRequest: UpdateApplicationFormRequest
    ): ResponseEntity<String> {
        applicationFormService.update(1L, updateApplicationFormRequest)
        return ResponseEntity.ok().build()
    }
}
