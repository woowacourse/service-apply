package apply.ui.api

import apply.application.AssignmentService
import apply.application.UserService
import apply.createAssignmentRequest
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

    @Test
    fun `과제물을 제출한다`() {
        val loginUser = createUser()

        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns loginUser.email
        every { userService.getByEmail(loginUser.email) } returns loginUser
        every { assignmentService.create(missionId, loginUser.id, createAssignmentRequest()) } just Runs

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/assignments", recruitmentId, missionId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
