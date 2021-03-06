package apply.ui.api

import apply.application.RecruitmentItemService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
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
    fun findAll(): ResponseEntity<ApiResponse<List<RecruitmentResponse>>> {
        return ResponseEntity.ok().body(ApiResponse.success(recruitmentService.findAllNotHidden()))
    }

    @GetMapping("/{id}/items")
    fun findItemsById(@PathVariable("id") recruitmentId: Long): ResponseEntity<ApiResponse<List<RecruitmentItem>>> {
        return ResponseEntity.ok()
            .body(ApiResponse.success(recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)))
    }
}
