package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionService
import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.createMissionResponse
import apply.createMyMissionResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(MissionRestController::class)
class MissionRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var missionService: MissionService

    @Test
    fun `과제를 추가한다`() {
        val response = createMissionResponse(id = 1L)
        every { missionService.save(any()) } returns response

        mockMvc.post("/api/recruitments/{recruitmentId}/missions", 1L) {
            jsonContent(createMissionData())
            bearer("valid_token")
        }.andExpect {
            status { isCreated }
            content { success(response) }
        }
    }

    @Test
    fun `과제를 조회한다`() {
        val response = createMissionResponse()
        every { missionService.getById(any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/{missionId}", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }
    }

    @Test
    fun `특정 모집의 모든 과제를 조회한다`() {
        val responses = listOf(
            MissionAndEvaluationResponse(createMission(), createEvaluation()),
            MissionAndEvaluationResponse(createMission(), createEvaluation())
        )
        every { missionService.findAllByRecruitmentId(any()) } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/missions", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(responses) }
        }
    }

    @Test
    fun `나의 과제들을 조회한다`() {
        val responses = listOf(createMyMissionResponse(id = 1L), createMyMissionResponse(id = 2L))
        every { missionService.findAllByUserIdAndRecruitmentId(any(), any()) } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/me", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(responses) }
        }.andDo {
            handle(document("mission-me-get"))
        }
    }

    @Test
    fun `과제를 삭제한다`() {
        every { missionService.deleteById(any()) } just Runs

        mockMvc.delete("/api/recruitments/{recruitmentId}/missions/{missionId}", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
