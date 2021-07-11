package apply.ui.api

import apply.application.ApplicantService
import apply.application.EvaluationData
import apply.application.EvaluationResponse
import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.config.RestDocsConfiguration
import apply.createEvaluation
import apply.createRecruitment
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [EvaluationRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestEnvironment
internal class EvaluationRestControllerTest(
    private val objectMapper: ObjectMapper,
) {

    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var evaluationService: EvaluationService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    internal fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentationContextProvider: RestDocumentationContextProvider,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(
                    restDocumentationContextProvider
                )
            )
            .build()
    }

    @Test
    internal fun `평가를 추가한다`() {
        val evaluationData = EvaluationData(createEvaluation(), createRecruitment(), null, emptyList())

        every { evaluationService.save(evaluationData) } returns Unit

        mockMvc.post("/api/evaluations") {
            content = objectMapper.writeValueAsBytes(evaluationData)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk }
            }
    }

    @Test
    internal fun `특정 평가를 조회한다`() {
        val evaluationData = EvaluationData(id = 1L)
        every { evaluationService.getDataById(evaluationData.id) } returns evaluationData

        mockMvc.get("/api/evaluations/{evaluationId}", evaluationData.id)
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(evaluationData))) }
            }
    }

    @Test
    internal fun `특정 모집의 모든 평가 정보들을 조회한다`() {
        val recruitmentId = 1L
        val expected = listOf(
            EvaluationSelectData("평가 항목 제목1", 1L),
            EvaluationSelectData("평가 항목 제목2", 2L)
        )
        every { evaluationService.getAllSelectDataByRecruitmentId(recruitmentId) } returns expected

        mockMvc.get("/api/recruitments/{recruitmentId}/evaluations", recruitmentId)
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(expected))) }
            }
    }

    @Test
    internal fun `모든 (상세한) 평가를 조회한다`() {
        val expected = listOf(
            EvaluationResponse(1L, "평가1", "평가1 설명", "우테코 3기 백엔드", 4L, "", 2L),
            EvaluationResponse(2L, "평가2", "평가2 설명", "우테코 3기 프론트", 5L, "", 2L),
        )
        every { evaluationService.findAllWithRecruitment() } returns expected

        mockMvc.get("/api/evaluations")
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(expected))) }
            }
    }

    @Test
    internal fun `평가를 삭제한다`() {
        val evaluationId = 1L
        every { evaluationService.deleteById(evaluationId) } returns Unit

        mockMvc.delete("/api/evaluations/{evaluationId}", evaluationId)
            .andExpect {
                status { isOk }
            }
    }
}
