package apply.ui.api

import apply.application.RecruitmentItemService
import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
import apply.domain.recruitmentitem.RecruitmentItem
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recruitments")
class RecruitmentRestController(
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService
) {
    @GetMapping
    fun findAll(): ResponseEntity<List<Recruitment>> {
        return ResponseEntity.ok().body(recruitmentService.findAll())
    }

    @GetMapping("/{id}/items")
    fun findItemsById(@PathVariable("id") recruitmentId: Long): ResponseEntity<List<RecruitmentItem>> {
        return ResponseEntity.ok().body(recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId))
    }
}
