package apply.ui.api

import apply.application.AssignmentService
import apply.application.UserService
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
import org.springframework.test.web.servlet.patch
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
    fun `과제물을 제출한다`() {
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
    fun `과제물을 수정한다`() {
        val loginUser = createUser()

        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns loginUser.email
        every { userService.getByEmail(loginUser.email) } returns loginUser
        every { assignmentService.update(missionId, loginUser.id, createAssignmentRequest()) } just Runs

        mockMvc.patch("/api/recruitments/{recruitmentId}/missions/{missionId}/assignments", recruitmentId, missionId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
