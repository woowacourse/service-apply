package apply.ui.api

import apply.application.RecruitmentData
import apply.application.RecruitmentItemService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.domain.member.Member
import apply.domain.recruitmentitem.RecruitmentItem
import apply.security.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri

@RequestMapping("/api/recruitments")
@RestController
class RecruitmentRestController(
    private val recruitmentService: RecruitmentService,
    private val recruitmentItemService: RecruitmentItemService
) {
    @PostMapping
    fun save(
        @RequestBody request: RecruitmentData,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<RecruitmentResponse>> {
        val response = recruitmentService.save(request)
        return ResponseEntity.created("/api/recruitments/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping
    fun findAllNotHidden(): ResponseEntity<ApiResponse<List<RecruitmentResponse>>> {
        val responses = recruitmentService.findAllNotHidden()
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @GetMapping("/all")
    fun findAll(
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<List<RecruitmentResponse>>> {
        val responses = recruitmentService.findAll()
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @GetMapping("/{recruitmentId}/items")
    fun findItemsById(
        @PathVariable recruitmentId: Long
    ): ResponseEntity<ApiResponse<List<RecruitmentItem>>> {
        val responses = recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @DeleteMapping("/{recruitmentId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<Unit> {
        recruitmentService.deleteById(recruitmentId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{recruitmentId}")
    fun getById(
        @PathVariable recruitmentId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<RecruitmentResponse>> {
        val response = recruitmentService.getById(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping("/{recruitmentId}/detail")
    fun getNotEndedDataById(
        @PathVariable recruitmentId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<RecruitmentData>> {
        val response = recruitmentService.getNotEndedDataById(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
