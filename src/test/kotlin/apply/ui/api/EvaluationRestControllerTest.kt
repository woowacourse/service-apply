package apply.ui.api

import apply.application.EvaluationData
import apply.application.EvaluationGridResponse
import apply.application.EvaluationResponse
import apply.application.EvaluationService
import apply.createEvaluation
import apply.createRecruitment
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(EvaluationRestController::class)
class EvaluationRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var evaluationService: EvaluationService

    @Test
    fun `평가를 추가한다`() {
        val response = EvaluationResponse(1L, "평가1", "평가1 설명", recruitmentId = 4L, beforeEvaluationId = 2L)
        every { evaluationService.save(any()) } returns response

        mockMvc.post("/api/recruitments/{recruitmentId}/evaluations", 1L) {
            jsonContent(EvaluationData(createEvaluation(), createRecruitment(), null, emptyList()))
            bearer("valid_token")
        }.andExpect {
            status { isCreated() }
            content { success(response) }
        }
    }

    @Test
    fun `특정 평가를 조회한다`() {
        val response = EvaluationResponse(1L, "평가1", "평가1 설명", recruitmentId = 4L, beforeEvaluationId = 2L)
        every { evaluationService.getById(any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `모든 (상세한) 평가를 조회한다`() {
        val responses = listOf(
            EvaluationGridResponse(1L, "평가1", "우테코 3기 백엔드", "이전 평가 없음"),
            EvaluationGridResponse(2L, "평가2", "우테코 3기 프론트", "평가1")
        )
        every { evaluationService.findAllWithRecruitment() } returns responses

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `평가를 삭제한다`() {
        every { evaluationService.deleteById(any()) } just Runs

        mockMvc.delete("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }
    }
}
