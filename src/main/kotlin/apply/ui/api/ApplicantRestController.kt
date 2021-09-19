package apply.ui.api

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/applicants")
class ApplicantRestController(
    private val applicantService: ApplicantService,
) {
    // @PostMapping("/register")
    // fun generateToken(@RequestBody @Valid request: RegisterApplicantRequest): ResponseEntity<ApiResponse<String>> {
    //     val token = applicantAuthenticationService.generateToken(request)
    //     return ResponseEntity.ok().body(ApiResponse.success(token))
    // }
    // TODO : 지원 기능 구현

    @GetMapping
    fun findAllByKeyword(
        @RequestParam keyword: String
    ): ResponseEntity<ApiResponse<List<ApplicantResponse>>> {
        val applicants = applicantService.findAllByKeyword(keyword)
        return ResponseEntity.ok(ApiResponse.success(applicants))
    }
}
