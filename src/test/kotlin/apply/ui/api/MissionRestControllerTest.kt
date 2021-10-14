package apply.ui.api

import apply.application.MissionAndEvaluationResponse
import apply.application.MissionService
import apply.application.UserService
import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.createMissionResponse
import apply.createUser
import apply.domain.evaluation.EvaluationRepository
import apply.security.JwtTokenProvider
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders.AUTHORIZATION
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
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var missionService: MissionService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var evaluationRepository: EvaluationRepository

    private val recruitmentId = 1L
    private val evaluationId = 1L
    private val missionId = 1L
    private val user = createUser(id = 1L)

    @Test
    fun `과제를 추가한다`() {
        every { missionService.save(createMissionData()) } just Runs

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions",
            recruitmentId
        ) {
            content = objectMapper.writeValueAsString(createMissionData())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `특정 모집의 모든 과제를 조회한다`() {
        val missionAndEvaluationResponses = listOf(
            MissionAndEvaluationResponse(createMission(), createEvaluation()),
            MissionAndEvaluationResponse(createMission(), createEvaluation())
        )
        every { missionService.findAllByRecruitmentId(recruitmentId) } returns missionAndEvaluationResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/missions", recruitmentId
        ).andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(missionAndEvaluationResponses))) }
        }
    }

    @Test
    fun `나의 과제들을 조회한다`() {
        val missionResponses = listOf(createMissionResponse(id = 1L), createMissionResponse(id = 2L))
        every { missionService.findAllByUserIdAndRecruitmentId(any(), any()) } returns missionResponses
        every { jwtTokenProvider.isValidToken(any()) } returns true
        every { jwtTokenProvider.getSubject(any()) } returns user.email
        every { userService.getByEmail(any()) } returns user

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/missions/me",
            recruitmentId
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(missionResponses))) }
        }
    }

    @Test
    fun `과제를 삭제한다`() {
        every { missionService.deleteById(missionId) } just Runs

        mockMvc.delete(
            "/api/recruitments/{recruitmentId}/missions/{missionId}",
            recruitmentId,
            missionId
        ).andExpect {
            status { isOk }
        }
    }

    @Test
    fun `과제가 있는 특정 평가의 과제를 조회한다`() {
        val mission = createMission()
        every { missionService.findByEvaluationId(missionId) } returns mission

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/missions",
            recruitmentId,
            evaluationId,
            missionId
        ).andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(mission))) }
        }
    }

    @Test
    fun `과제가 없는 특정 평가의 과제를 조회한다`() {
        every { missionService.findByEvaluationId(missionId) } returns null

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/missions",
            recruitmentId,
            evaluationId,
            missionId
        ).andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(null))) }
        }
    }
}
