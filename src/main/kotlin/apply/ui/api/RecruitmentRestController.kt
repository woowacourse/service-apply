package apply.ui.api

import apply.application.RecruitmentData
import apply.application.RecruitmentItemService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/recruitments")
class RecruitmentRestController(
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService
) {
    @GetMapping
    fun findAllNotHidden(): ResponseEntity<ApiResponse<List<RecruitmentResponse>>> {
        return ResponseEntity.ok().body(ApiResponse.success(recruitmentService.findAllNotHidden()))
    }

    @GetMapping("/all")
    fun findAll(
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<RecruitmentResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(recruitmentService.findAll()))
    }

    @GetMapping("/{recruitmentId}/items")
    fun findItemsById(
        @PathVariable recruitmentId: Long
    ): ResponseEntity<ApiResponse<List<RecruitmentItem>>> {
        return ResponseEntity.ok()
            .body(ApiResponse.success(recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)))
    }

    @PostMapping
    fun save(
        @RequestBody request: RecruitmentData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        val savedId = recruitmentService.save(request)
        return ResponseEntity.created(URI.create("/api/recruitments/$savedId")).build()
    }

    @DeleteMapping("/{recruitmentId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        recruitmentService.deleteById(recruitmentId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{recruitmentId}")
    fun getById(
        @PathVariable recruitmentId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<Recruitment>> {
        return ResponseEntity.ok(ApiResponse.success(recruitmentService.getById(recruitmentId)))
    }

    @GetMapping("/{recruitmentId}/detail")
    fun getNotEndedDataById(
        @PathVariable recruitmentId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<RecruitmentData>> {
        return ResponseEntity.ok(ApiResponse.success(recruitmentService.getNotEndedDataById(recruitmentId)))
    }
}
