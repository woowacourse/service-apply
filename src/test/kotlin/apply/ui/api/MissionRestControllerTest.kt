package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionService
import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.createMissionResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [MissionRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class MissionRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var missionService: MissionService

    private val recruitmentId = 1L

    @Test
    fun `과제를 추가한다`() {
        val missionId = 1L
        every { missionService.save(any()) } returns missionId

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions",
            recruitmentId
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createMissionData())
        }.andExpect {
            status { isCreated }
            header { string(HttpHeaders.LOCATION, "/api/recruitments/$recruitmentId/missions/$missionId") }
        }
    }

    @Test
    fun `특정 모집의 모든 과제를 조회한다`() {
        val missionAndEvaluationResponses = listOf(
            MissionAndEvaluationResponse(createMission(), createEvaluation()),
            MissionAndEvaluationResponse(createMission(), createEvaluation())
        )
        every { missionService.findAllByRecruitmentId(any()) } returns missionAndEvaluationResponses

        mockMvc.get("/api/recruitments/{recruitmentId}/missions", recruitmentId) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(missionAndEvaluationResponses))) }
        }
    }

    @Test
    fun `나의 과제들을 조회한다`() {
        val missionResponses = listOf(createMissionResponse(id = 1L), createMissionResponse(id = 2L))
        every { missionService.findAllByUserIdAndRecruitmentId(any(), any()) } returns missionResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/missions/me",
            recruitmentId
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(missionResponses))) }
        }
    }

    @Test
    fun `과제를 삭제한다`() {
        every { missionService.deleteById(any()) } just Runs

        mockMvc.delete(
            "/api/recruitments/{recruitmentId}/missions/{missionId}",
            recruitmentId,
            1L
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
