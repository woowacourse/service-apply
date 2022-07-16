package apply.ui.api

import apply.application.AssignmentService
import apply.createAssignmentData
import apply.createAssignmentRequest
import apply.createAssignmentResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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

    private val recruitmentId = 1L
    private val missionId = 1L

    @Test
    fun `과제 제출물을 제출한다`() {
        val assignmentId = 1L
        every { assignmentService.create(any(), any(), createAssignmentRequest()) } returns assignmentId

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments",
            recruitmentId,
            missionId
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            header {
                string(
                    HttpHeaders.LOCATION,
                    "/api/recruitments/$recruitmentId/missions/$missionId/assignments/$assignmentId"
                )
            }
        }
    }

    @Test
    fun `나의 과제 제출물을 조회한다`() {
        val assignmentResponse = createAssignmentResponse()
        every { assignmentService.getByUserIdAndMissionId(any(), any()) } returns assignmentResponse

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments/me",
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
        val assignmentData = createAssignmentData()
        every { assignmentService.findByEvaluationTargetId(any()) } returns assignmentData
        mockMvc.get(
            "/api/recruitments/{recruitmentId}/targets/{targetId}/assignments",
            recruitmentId,
            1L,
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(assignmentData))) }
        }
    }
}
