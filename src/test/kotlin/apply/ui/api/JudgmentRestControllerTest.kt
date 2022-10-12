package apply.ui.api

import apply.application.JudgmentAllService
import apply.application.JudgmentService
import apply.createCancelJudgmentRequest
import apply.createFailJudgmentRequest
import apply.createLastJudgmentResponse
import apply.createSuccessJudgmentRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(JudgmentRestController::class)
class JudgmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var judgmentService: JudgmentService

    @MockkBean
    private lateinit var judgmentAllService: JudgmentAllService

    @Test
    fun `예제 테스트를 실행한다`() {
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
    fun `예제 테스트 결과를 조회한다`() {
        val response = createLastJudgmentResponse()
        every { judgmentService.findExample(any(), any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/missions/{missionId}/judgments/judge-example", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }.andDo {
            handle(document("judgment-judge-example-get"))
        }
    }

    @Test
    fun `자동 채점 성공 결과를 반영한다`() {
        every { judgmentService.success(any(), any()) } just Runs

        mockMvc.post("/api/judgments/{judgmentId}/success", 1L) {
            jsonContent(createSuccessJudgmentRequest())
        }.andExpect {
            status { isOk }
        }.andDo {
            handle(document("judgment-success-post"))
        }
    }

    @Test
    fun `자동 채점 실패 결과를 반영한다`() {
        every { judgmentService.fail(any(), any()) } just Runs

        mockMvc.post("/api/judgments/{judgmentId}/fail", 1L) {
            jsonContent(createFailJudgmentRequest())
        }.andExpect {
            status { isOk }
        }.andDo {
            handle(document("judgment-fail-post"))
        }
    }

    @Test
    fun `자동 채점 취소 결과를 반영한다`() {
        every { judgmentService.cancel(any(), any()) } just Runs

        mockMvc.post("/api/judgments/{judgmentId}/cancel", 1L) {
            jsonContent(createCancelJudgmentRequest())
        }.andExpect {
            status { isOk }
        }.andDo {
            handle(document("judgment-cancel-post"))
        }
    }

    @Test
    fun `본 자동 채점을 실행한다`() {
        val response = createLastJudgmentResponse()
        every { judgmentService.judgeReal(any()) } returns response

        mockMvc.post("/api/evaluations/{evaluationId}/assignments/{assignmentId}/judgments/judge-real", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }
    }

    @Test
    fun `전체 자동 채점을 실행한다`() {
        every { judgmentAllService.judgeAll(any()) } just Runs

        mockMvc.post("/api/evaluations/{evaluationId}/judgments/judge-all", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
