package apply.ui.api

import apply.application.AssignmentService
import apply.application.UserService
import apply.createAssignmentData
import apply.createAssignmentRequest
import apply.createAssignmentResponse
import apply.createUser
import apply.security.JwtTokenProvider
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [AssignmentRestController::class]
)
@TestEnvironment
internal class AssignmentRestControllerTest : RestControllerTest() {

    @MockkBean
    private lateinit var assignmentService: AssignmentService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var userService: UserService

    private val recruitmentId = 1L
    private val missionId = 1L
    private val loginUser = createUser()

    @Test
    fun `과제 제출물을 제출한다`() {
        every { jwtTokenProvider.isValidToken(any()) } returns true
        every { jwtTokenProvider.getSubject(any()) } returns loginUser.email
        every { userService.getByEmail(any()) } returns loginUser
        every { assignmentService.create(any(), any(), createAssignmentRequest()) } just Runs

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments",
            recruitmentId,
            missionId
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `나의 과제 제출물을 조회한다`() {
        val assignmentResponse = createAssignmentResponse()
        every { jwtTokenProvider.isValidToken(any()) } returns true
        every { jwtTokenProvider.getSubject(any()) } returns loginUser.email
        every { userService.getByEmail(any()) } returns loginUser
        every { assignmentService.getByUserIdAndMissionId(any(), any()) } returns assignmentResponse

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments",
            recruitmentId,
            missionId
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(assignmentResponse))) }
        }
    }

    @Test
    fun `특정 평가 대상자의 특정 과제에 해당하는 과제 제출물을 조회한다`() {
        val evaluationId = 1L
        val targetId = 1L
        val assignmentData = createAssignmentData()
        every { assignmentService.findByEvaluationTargetId(targetId) } returns assignmentData
        mockMvc.get(
            "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/missions/{missionId}/targets/{targetId}/assignments",
            recruitmentId,
            evaluationId,
            missionId,
            targetId
        ) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(assignmentData))) }
        }
    }
}
