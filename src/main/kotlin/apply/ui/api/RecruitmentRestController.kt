package apply.ui.api

import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RecruitmentRestController(private val recruitmentService: RecruitmentService) {
    @GetMapping("/api/recruitments")
    fun findAll(): ResponseEntity<List<Recruitment>> {
        return ResponseEntity.ok().body(recruitmentService.findAll())
    }
}
