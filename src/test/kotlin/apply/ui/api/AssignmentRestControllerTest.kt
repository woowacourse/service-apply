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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(AssignmentRestController::class)
class AssignmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var assignmentService: AssignmentService

    @Test
    fun `과제 제출물을 제출한다`() {
        val response = createAssignmentResponse()
        every { assignmentService.create(any(), any(), any()) } returns response

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/assignments", 1L, 1L) {
            jsonContent(createAssignmentRequest())
            bearer("valid_token")
        }.andExpect {
            status { isCreated() }
            content { success(response) }
        }.andDo {
            handle(document("assignment-post"))
        }
    }

    @Test
    fun `과제 제출물을 수정한다`() {
        every { assignmentService.update(any(), any(), any()) } just Runs

        mockMvc.patch("/api/recruitments/{recruitmentId}/missions/{missionId}/assignments", 1L, 1L) {
            jsonContent(createAssignmentRequest())
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }.andDo {
            handle(document("assignment-patch"))
        }
    }

    @Test
    fun `나의 과제 제출물을 조회한다`() {
        val response = createAssignmentResponse()
        every { assignmentService.getByMemberIdAndMissionId(any(), any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/{missionId}/assignments/me", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("assignment-me-get"))
        }
    }

    @Test
    fun `특정 평가 대상자의 특정 과제에 해당하는 과제 제출물을 조회한다`() {
        val response = createAssignmentData()
        every { assignmentService.findByEvaluationTargetId(any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/targets/{targetId}/assignments", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }
}
