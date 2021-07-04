package apply.ui.api

import apply.application.ApplicantService
import apply.application.EvaluationData
import apply.application.EvaluationResponse
import apply.application.EvaluationSelectData
import apply.application.EvaluationService
import apply.application.RecruitmentSelectData
import apply.config.RestDocsConfiguration
import apply.createEvaluation
import apply.createRecruitment
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
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
    private val objectMapper: ObjectMapper
) {

    @MockBean
    private lateinit var applicantService: ApplicantService

    @MockBean
    private lateinit var evaluationService: EvaluationService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    internal fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentationContextProvider: RestDocumentationContextProvider
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

        mockMvc.post("/api/evaluations") {
            content = objectMapper.writeValueAsBytes(evaluationData)
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk }
            }
    }

    @Test
    internal fun `모든 모집 선택 정보를 조회한다`() {
        val expected = listOf(RecruitmentSelectData("평가1", 1L), RecruitmentSelectData("평가2", 2L))
        given(evaluationService.findAllRecruitmentSelectData()).willReturn(expected)

        mockMvc.get("/api/evaluations/recruitments")
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(expected))) }
            }
    }

    @Test
    internal fun `특정 평가를 조회한다`() {
        val evaluationData = EvaluationData(id = 1L)
        given(evaluationService.getDataById(evaluationData.id)).willReturn(evaluationData)

        mockMvc.get("/api/evaluations/{evaluationId}", evaluationData.id)
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(evaluationData))) }
            }
    }

    @Test
    internal fun `특정 모집의 모든 평가 정보들을 조회한다`() {
        val expected = listOf(EvaluationSelectData(), EvaluationSelectData()) // todo: 데이터 채워넣기...
        given(evaluationService.getAllSelectDataByRecruitmentId(1L))
            .willReturn(expected)

        mockMvc.get("/api/evaluations/info") {
            param("recruitmentId", "1")
        }
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
        given(evaluationService.findAllWithRecruitment()).willReturn(expected)

        mockMvc.get("/api/evaluations")
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(expected))) }
            }
    }

    @Test
    internal fun `평가를 삭제한다`() {
        val evaluationId = 1L
        mockMvc.delete("/api/evaluations/{evaluationId}", evaluationId)
            .andExpect {
                status { isOk }
            }
    }
}
