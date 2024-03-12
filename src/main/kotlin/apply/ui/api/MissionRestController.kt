package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MissionService
import apply.application.MyMissionResponse
import apply.application.MyMissionService
import apply.domain.member.Member
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

@RequestMapping("/api/recruitments/{recruitmentId}/missions")
@RestController
class MissionRestController(
    private val missionService: MissionService,
    private val missionQueryService: MyMissionService
) {
    @PostMapping
    fun save(
        @PathVariable recruitmentId: Long,
        @RequestBody missionData: MissionData,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<MissionResponse>> {
        val response = missionService.save(missionData)
        return ResponseEntity.created("/api/recruitments/$recruitmentId/missions/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping("/{missionId}")
    fun getById(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<MissionResponse>> {
        val response = missionService.getById(missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping
    fun findAllByRecruitmentId(
        @PathVariable recruitmentId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<ApiResponse<List<MissionAndEvaluationResponse>>> {
        val responses = missionService.findAllByRecruitmentId(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @GetMapping("/me")
    fun findMyMissionsByRecruitmentId(
        @PathVariable recruitmentId: Long,
        @LoginMember member: Member
    ): ResponseEntity<ApiResponse<List<MyMissionResponse>>> {
        val responses = missionQueryService.findAllByMemberIdAndRecruitmentId(member.id, recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @DeleteMapping("/{missionId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginMember(administrator = true) member: Member
    ): ResponseEntity<Unit> {
        missionService.deleteById(missionId)
        return ResponseEntity.ok().build()
    }
}
