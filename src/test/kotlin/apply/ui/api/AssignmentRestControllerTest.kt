package apply.ui.api

import apply.application.AssignmentService
import apply.createAssignmentRequest
import apply.createUser
import apply.security.LoginUserResolver
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
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
    private lateinit var loginUserResolver: LoginUserResolver

    private val recruitmentId = 1L
    private val evaluationId = 1L
    private val missionId = 1L

    @Test
    fun `과제물을 제출한다`() {
        val loginApplicant = createUser()
        every { loginUserResolver.supportsParameter(any()) } returns true
        every { loginUserResolver.resolveArgument(any(), any(), any(), any()) } returns loginApplicant
        every { assignmentService.create(missionId, loginApplicant.id, createAssignmentRequest()) } just Runs

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments",
            recruitmentId,
            evaluationId,
            missionId
        ) {
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
        }
    }
}
