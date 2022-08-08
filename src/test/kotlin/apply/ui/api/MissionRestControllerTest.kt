package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionService
import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.createMissionResponse
import apply.createMyMissionResponse
import apply.ui.api.ApiResponse.Companion.success
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(
    controllers = [MissionRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
class MissionRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var missionService: MissionService

    private val recruitmentId = 1L

    @Test
    fun `과제를 추가한다`() {
        val missionResponse = createMissionResponse(id = 1L)
        every { missionService.save(any()) } returns missionResponse

        mockMvc.post("/api/recruitments/{recruitmentId}/missions", recruitmentId) {
            content = objectMapper.writeValueAsString(createMissionData())
            contentType = APPLICATION_JSON
            bearer("valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(success(missionResponse))) }
        }
    }

    @Test
    fun `과제를 조회한다`() {
        val response = createMissionResponse()
        every { missionService.getById(any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/{missionId}", recruitmentId, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(response))) }
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
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(missionAndEvaluationResponses))) }
        }
    }

    @Test
    fun `나의 과제들을 조회한다`() {
        val myMissionResponses = listOf(createMyMissionResponse(id = 1L), createMyMissionResponse(id = 2L))
        every { missionService.findAllByUserIdAndRecruitmentId(any(), any()) } returns myMissionResponses

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/me", recruitmentId) {
            contentType = APPLICATION_JSON
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(myMissionResponses))) }
        }.andDo {
            handle(document("get/recruitments/missions/me"))
        }
    }

    @Test
    fun `과제를 삭제한다`() {
        every { missionService.deleteById(any()) } just Runs

        mockMvc.delete("/api/recruitments/{recruitmentId}/missions/{missionId}", recruitmentId, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
