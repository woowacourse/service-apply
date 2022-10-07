package apply.ui.api

import apply.application.JudgmentService
import apply.createJudgmentFailRequest
import apply.createJudgmentSuccessRequest
import apply.createLastJudgmentResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(JudgmentRestController::class)
class JudgmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var judgmentService: JudgmentService

    @Test
    fun `예제 채점을 실행한다`() {
        val response = createLastJudgmentResponse()
        every { judgmentService.judgeExample(any(), any()) } returns response

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-example", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }.andDo {
            handle(document("judgment-judge-example-post"))
        }
    }

    @Test
    fun `본 채점을 실행한다`() {
        val response = createLastJudgmentResponse()
        every { judgmentService.judgeReal(any(), any()) } returns response

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-real", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }.andDo {
            handle(document("judgment-judge-real-post"))
        }
    }

    @Test
    fun `성공 결과를 수신한다`() {
        every { judgmentService.success(any(), any()) } just Runs

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/judgments/pass", 1L, 1L) {
            jsonContent(createJudgmentSuccessRequest())
        }.andExpect {
            status { isOk }
        }.andDo {
            handle(document("judgment-success-result-post"))
        }
    }

    @Test
    fun `실패 결과를 수신한다`() {
        every { judgmentService.fail(any(), any()) } just Runs

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/judgments/fail", 1L, 1L) {
            jsonContent(createJudgmentFailRequest())
        }.andExpect {
            status { isOk }
        }.andDo {
            handle(document("judgment-fail-result-post"))
        }
    }
}
