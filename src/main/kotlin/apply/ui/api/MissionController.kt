package apply.ui.api

import apply.application.MissionData
import apply.application.MissionResponse
import apply.application.MissionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}")
class MissionController(
    private val missionService: MissionService
) {
    @PostMapping("/evaluations/{evaluationId}/missions")
    fun createMission(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestBody missionData: MissionData
    ): ResponseEntity<Unit> {
        missionService.save(missionData)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/missions")
    fun findMissionsByRecruitmentId(@PathVariable recruitmentId: Long): ResponseEntity<ApiResponse<List<MissionResponse>>> {
        val missions = missionService.findAllByRecruitmentId(recruitmentId)
        return ResponseEntity.ok(ApiResponse.success(missions))
    }

    @DeleteMapping("/missions/{missionId}")
    fun deleteMission(@PathVariable recruitmentId: Long, @PathVariable missionId: Long): ResponseEntity<Unit> {
        missionService.deleteById(missionId)
        return ResponseEntity.ok().build()
    }
}