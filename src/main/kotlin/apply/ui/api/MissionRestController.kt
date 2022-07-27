package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MissionService
import apply.application.MyMissionResponse
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
import support.toUri

@RequestMapping("/api/recruitments/{recruitmentId}/missions")
@RestController
class MissionRestController(
    private val missionService: MissionService
) {
    @PostMapping
    fun save(
        @PathVariable recruitmentId: Long,
        @RequestBody missionData: MissionData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<MissionResponse>> {
        val response = missionService.save(missionData)
        return ResponseEntity.created("/api/recruitments/$recruitmentId/missions/${response.id}".toUri())
            .body(ApiResponse.success(response))
    }

    @GetMapping("/{missionId}")
    fun getById(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<MissionResponse>> {
        val response = missionService.getById(missionId)
        return ResponseEntity.ok(ApiResponse.success(response))
    }

    @GetMapping
    fun findAllByRecruitmentId(
        @PathVariable recruitmentId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<MissionAndEvaluationResponse>>> {
        val responses = missionService.findAllByRecruitmentId(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @GetMapping("/me")
    fun findMyMissionsByRecruitmentId(
        @PathVariable recruitmentId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<List<MyMissionResponse>>> {
        val responses = missionService.findAllByUserIdAndRecruitmentId(user.id, recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(responses))
    }

    @DeleteMapping("/{missionId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        missionService.deleteById(missionId)
        return ResponseEntity.ok().build()
    }
}
