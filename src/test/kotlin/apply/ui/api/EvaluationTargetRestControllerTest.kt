package apply.ui.api

import apply.NOTE
import apply.application.ApplicantService
import apply.application.EvaluationItemResponse
import apply.application.EvaluationItemScoreData
import apply.application.EvaluationTargetData
import apply.application.EvaluationTargetResponse
import apply.application.EvaluationTargetService
import apply.application.GradeEvaluationResponse
import apply.config.RestDocsConfiguration
import apply.createEvaluationItem
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.security.JwtTokenProvider
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
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [EvaluationTargetRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class)
@TestEnvironment
internal class EvaluationTargetRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var evaluationTargetService: EvaluationTargetService

    private lateinit var mockMvc: MockMvc

    private val recruitmentId = 1L
    private val evaluationId = 1L
    private val targetId = 1L
    private val keyword = "아마찌"
    private val updatedScore = 5
    private val updatedStatus = EvaluationStatus.PASS
    private val updatedNote = "특이 사항(수정)"
    private val answers = listOf(EvaluationItemScoreData(score = updatedScore, id = 3L))
    private val gradeEvaluationRequest = EvaluationTargetData(answers, updatedNote, updatedStatus)

    private val evaluationTargetResponse: EvaluationTargetResponse = EvaluationTargetResponse(
        id = 1L,
        name = "아마찌",
        email = "wlgp2500@gmail.com",
        totalScore = 100,
        evaluationStatus = EvaluationStatus.PASS,
        administratorId = 1L,
        note = NOTE,
        answers = EvaluationAnswers(mutableListOf()),
    )

    private val gradeEvaluationResponse: GradeEvaluationResponse = GradeEvaluationResponse(
        title = "평가 항목 제목",
        description = "평가 항목 설명",
        evaluationTarget = EvaluationTargetData(),
        evaluationItems = listOf(EvaluationItemResponse(createEvaluationItem()))
    )

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
    fun `평가 id와 키워드(이름, 이메일)로 평가 대상자를 조회한다`() {
        every { evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId, keyword) }.answers {
            listOf(evaluationTargetResponse)
        }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{keyword}",
                recruitmentId,
                evaluationId,
                keyword
            )
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(listOf(evaluationTargetResponse))
                    )
                )
            )
            .andDo(
                document(
                    "evaluation-target-findAllByEvaluationIdAndKeyword",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                        parameterWithName("keyword").description("키워드(이름, 이메일)")
                    ),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.[]").description("평가 대상자 목록"),
                    ).andWithPrefix("body.[].", EVALUATION_TARGET_FIELD_DESCRIPTORS)
                )
            )
    }

    @Test
    fun `이전 평가와 평가 대상자 존재 여부를 통해 저장하거나 갱신한다 `() {
        every { evaluationTargetService.load(evaluationId) } returns Unit

        mockMvc.perform(
            RestDocumentationRequestBuilders.put(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/renew",
                recruitmentId, evaluationId,
            )
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "evaluation-target-load",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                    ),
                )
            )
    }

    @Test
    fun `평가 대상 id로 채점 정보를 불러온다`() {
        every { evaluationTargetService.getGradeEvaluation(targetId) }.answers { gradeEvaluationResponse }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade",
                recruitmentId,
                evaluationId,
                targetId
            )
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(gradeEvaluationResponse)
                    )
                )
            )
            .andDo(
                document(
                    "evaluation-target-getGradeEvaluation",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                        parameterWithName("targetId").description("평가 대상자 ID"),
                    ),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.title").type(JsonFieldType.STRING).description("평가 항목 제목"),
                        fieldWithPath("body.description").type(JsonFieldType.STRING).description("평가 항목 설명"),
                        fieldWithPath("body.evaluationTarget").type(JsonFieldType.OBJECT).description("평가 대상자 정보"),
                        fieldWithPath("body.evaluationTarget.evaluationItemScores").type(JsonFieldType.ARRAY)
                            .description("평가 항목 점수 목록"),
                        fieldWithPath("body.evaluationTarget.note").type(JsonFieldType.STRING).description("평가 특이 사항"),
                        fieldWithPath("body.evaluationTarget.evaluationStatus").type(JsonFieldType.STRING)
                            .description("평가 상태"),
                        fieldWithPath("body.evaluationItems").type(JsonFieldType.ARRAY).description("평가 항목 목록"),
                        fieldWithPath("body.evaluationItems.[].title").type(JsonFieldType.STRING)
                            .description("평가 항목 제목"),
                        fieldWithPath("body.evaluationItems.[].description").type(JsonFieldType.STRING)
                            .description("평가 항목 설명"),
                        fieldWithPath("body.evaluationItems.[].maximumScore").type(JsonFieldType.NUMBER)
                            .description("평가 항목 최대 점수"),
                        fieldWithPath("body.evaluationItems.[].position").type(JsonFieldType.NUMBER)
                            .description("평가 항목 POSITION"),
                        fieldWithPath("body.evaluationItems.[].evaluationId").type(JsonFieldType.NUMBER)
                            .description("평가 ID"),
                        fieldWithPath("body.evaluationItems.[].id").type(JsonFieldType.NUMBER).description("평가 항목 ID"),
                    )
                )
            )
    }

    @Test
    fun `평가 완료 후 점수를 매긴다`() {
        every { evaluationTargetService.grade(targetId, gradeEvaluationRequest) } returns Unit

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch(
                "/api/recruitments/{recruitmentId}/evaluations/{evaluationId}/targets/{targetId}/grade",
                recruitmentId,
                evaluationId,
                targetId
            ).header("Content-Type", "application/json")
                .content(
                    objectMapper.writeValueAsString(
                        gradeEvaluationRequest
                    )
                )
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "evaluationtarget-grade",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("recruitmentId").description("모집 ID"),
                        parameterWithName("evaluationId").description("평가 ID"),
                        parameterWithName("targetId").description("평가 대상자 ID"),
                    ),
                    requestFields(
                        fieldWithPath("evaluationItemScores").type(JsonFieldType.ARRAY)
                            .description("평가 항목 점수 목록"),
                        fieldWithPath("evaluationItemScores.[].score").type(JsonFieldType.NUMBER)
                            .description("평가 항목 점수"),
                        fieldWithPath("evaluationItemScores.[].id").type(JsonFieldType.NUMBER)
                            .description("평가 항목 점수 ID"),
                        fieldWithPath("note").type(JsonFieldType.STRING)
                            .description("평가 특이 사항"),
                        fieldWithPath("evaluationStatus").type(JsonFieldType.STRING)
                            .description("평가 상태"),
                    )
                )
            )
    }

    private fun getDocumentRequest(): OperationRequestPreprocessor {
        return Preprocessors.preprocessRequest(
            Preprocessors.modifyUris()
                .scheme("https")
                .host("woowa")
                .removePort(),
            Preprocessors.prettyPrint()
        )
    }

    private fun getDocumentResponse(): OperationResponsePreprocessor {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    }

    companion object {
        val EVALUATION_TARGET_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
            fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
            fieldWithPath("totalScore").type(JsonFieldType.NUMBER).description("점수"),
            fieldWithPath("evaluationStatus").type(JsonFieldType.STRING).description("평가 상태"),
            fieldWithPath("administratorId").type(JsonFieldType.NUMBER).description("평가자 ID"),
            fieldWithPath("note").type(JsonFieldType.STRING).description("평가 특이 사항"),
            fieldWithPath("answers").type(JsonFieldType.ARRAY).description("평가 대답") // TODO 확인 필요
        )
    }
}
