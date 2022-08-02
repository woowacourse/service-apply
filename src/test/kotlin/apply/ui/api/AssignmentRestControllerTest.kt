package apply.ui.api

import apply.application.AssignmentService
import apply.createAssignmentData
import apply.createAssignmentRequest
import apply.createAssignmentResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [AssignmentRestController::class]
)
@TestEnvironment
class AssignmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var assignmentService: AssignmentService

    private val recruitmentId = 1L
    private val missionId = 1L

    @Test
    fun `과제 제출물을 제출한다`() {
        every { assignmentService.create(any(), any(), any()) } returns createAssignmentResponse()

        mockMvc.post(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments",
            recruitmentId,
            missionId
        ) {
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(createAssignmentResponse()))) }
        }.andDo {
            handle(document("assignment-create"))
        }
    }

    @Test
    fun `과제 제출물을 수정한다`() {
        every { assignmentService.update(any(), any(), any()) } just Runs

        mockMvc.patch(
            "/api/recruitments/{recruitmentId}/missions/{missionId}/assignments",
            recruitmentId,
            missionId
        ) {
            content = objectMapper.writeValueAsString(createAssignmentRequest())
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }.andDo {
            handle(document("assignment-update"))
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
        }.andDo {
            handle(document("assignment-me"))
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
