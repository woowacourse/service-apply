package apply.ui.api

import apply.application.JudgmentService
import apply.createLastJudgmentResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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
}
