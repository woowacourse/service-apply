package apply.ui.api

import apply.application.AgreementResponse
import apply.application.AgreementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/agreements")
@RestController
class AgreementRestController(
    private val agreementService: AgreementService,
) {
    @GetMapping("/latest")
    fun latest(): ResponseEntity<ApiResponse<AgreementResponse>> {
        return ResponseEntity.ok(ApiResponse.success(agreementService.latest()))
    }
}
