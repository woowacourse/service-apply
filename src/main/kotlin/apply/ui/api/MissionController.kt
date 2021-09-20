package apply.ui.api

import apply.application.MissionData
import apply.application.MissionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/missions")
class MissionController(
    private val missionService: MissionService
) {
    @PostMapping
    fun createMission(
        @PathVariable recruitmentId: Long,
        @PathVariable evaluationId: Long,
        @RequestBody missionData: MissionData
    ): ResponseEntity<Unit> {
        missionService.save(missionData)
        return ResponseEntity.ok().build()
    }
}
