package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MissionService
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
@RequestMapping("/api/recruitments/{recruitmentId}")
class MissionRestController(
    private val missionService: MissionService
) {
    @PostMapping("/missions")
    fun save(
        @PathVariable recruitmentId: Long,
        @RequestBody missionData: MissionData,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        val saveId = missionService.save(missionData)
        return ResponseEntity.created(URI.create("/api/recruitments/$recruitmentId/missions/$saveId")).build()
    }

    @GetMapping("/missions")
    fun findAllByRecruitmentId(
        @PathVariable recruitmentId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<List<MissionAndEvaluationResponse>>> {
        val missions = missionService.findAllByRecruitmentId(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(missions))
    }

    @GetMapping("/missions/me")
    fun findMyMissionsByRecruitmentId(
        @PathVariable recruitmentId: Long,
        @LoginUser user: User
    ): ResponseEntity<ApiResponse<List<MissionResponse>>> {
        val missions = missionService.findAllByUserIdAndRecruitmentId(user.id, recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(missions))
    }

    @DeleteMapping("/missions/{missionId}")
    fun deleteById(
        @PathVariable recruitmentId: Long,
        @PathVariable missionId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<Unit> {
        missionService.deleteById(missionId)
        return ResponseEntity.ok().build()
    }
}
