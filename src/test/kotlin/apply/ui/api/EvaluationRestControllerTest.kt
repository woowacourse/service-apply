package apply.ui.api

import apply.application.EvaluationData
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
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [EvaluationRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class EvaluationRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var evaluationService: EvaluationService

    private val evaluationResponses: List<EvaluationResponse> = listOf(
        EvaluationResponse(1L, "평가1", "평가1 설명", "우테코 3기 백엔드", 4L, "", 2L),
        EvaluationResponse(2L, "평가2", "평가2 설명", "우테코 3기 프론트", 5L, "", 2L),
    )

    @Test
    fun `평가를 추가한다`() {
        every { evaluationService.save(any()) } returns evaluationResponses[0]

        mockMvc.post("/api/recruitments/{recruitmentId}/evaluations", 1L) {
            content = objectMapper.writeValueAsBytes(
                EvaluationData(createEvaluation(), createRecruitment(), null, emptyList())
            )
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(evaluationResponses[0]))) }
        }
    }

    @Test
    fun `특정 평가를 조회한다`() {
        val evaluationData = EvaluationData(id = 1L)
        every { evaluationService.getDataById(any()) } returns evaluationData

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}", 1L, evaluationData.id) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(evaluationData))) }
        }
    }

    @Test
    fun `모든 (상세한) 평가를 조회한다`() {
        every { evaluationService.findAllWithRecruitment() } returns evaluationResponses

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations", 1L) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(evaluationResponses))) }
        }
    }

    @Test
    fun `평가를 삭제한다`() {
        every { evaluationService.deleteById(any()) } just Runs

        mockMvc.delete("/api/recruitments/{recruitmentId}/evaluations/{evaluationId}", 1L, 1L) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
