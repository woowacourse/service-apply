package apply.ui.api

import apply.application.JudgmentService
import apply.application.MissionAndEvaluationResponse
import apply.application.MissionService
import apply.createEvaluation
import apply.createLastJudgmentResponse
import apply.createMission
import apply.createMissionData
import apply.createMissionJudgmentResponse
import apply.createMissionResponse
import apply.createMyMissionResponse
import apply.domain.judgment.JudgmentStatus
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

    @MockkBean
    private lateinit var judgmentService: JudgmentService

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
        val missionResponses =
            listOf(createMyMissionResponse(id = 1L), createMyMissionResponse(id = 2L), createMyMissionResponse(id = 3L))
        val lastJudgmentResponses =
            listOf(
                null,
                createLastJudgmentResponse(),
                createLastJudgmentResponse(passCount = 9, totalCount = 10, status = JudgmentStatus.SUCCEEDED)
            )
        val expected = listOf(
            createMissionJudgmentResponse(id = 1L, isAutomation = false, judgment = lastJudgmentResponses[0]),
            createMissionJudgmentResponse(id = 2L, isAutomation = true, judgment = lastJudgmentResponses[1]),
            createMissionJudgmentResponse(id = 3L, isAutomation = true, judgment = lastJudgmentResponses[2])
        )

        every { missionService.findAllByUserIdAndRecruitmentId(any(), any()) } returns missionResponses
        every { judgmentService.findExample(any(), 1L) } returns lastJudgmentResponses[0]
        every { judgmentService.findExample(any(), 2L) } returns lastJudgmentResponses[1]
        every { judgmentService.findExample(any(), 3L) } returns lastJudgmentResponses[2]

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/me", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(expected) }
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
