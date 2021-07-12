package apply.ui.api

import apply.application.ApplicantService
import apply.application.RecruitmentItemService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.config.RestDocsConfiguration
import apply.createRecruitment
import apply.createRecruitmentData
import apply.createRecruitmentItem
import apply.createRecruitmentItemData
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
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.junit.jupiter.SpringExtension
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
    controllers = [RecruitmentRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@Import(RestDocsConfiguration::class)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@TestEnvironment
internal class RecruitmentRestControllerTest(
    private val objectMapper: ObjectMapper
) {

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var recruitmentService: RecruitmentService

    @MockkBean
    private lateinit var recruitmentItemService: RecruitmentItemService

    private lateinit var mockMvc: MockMvc

    private val recruitmentId = 1L

    val recruitment = createRecruitment()

    private val recruitmentResponse = RecruitmentResponse(recruitment)

    private val recruitmentItems = listOf(createRecruitmentItem())

    private val recruitmentData = createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData()))

    private val recruitments = listOf(recruitment)

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
    fun `공개된 지원서 목록을 가져온다`() {
        every { recruitmentService.findAllNotHidden() } answers { listOf(recruitmentResponse) }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments"
            )
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "recruitment-findAllNotHidden",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.[]").description("모집 목록"),
                    ).andWithPrefix("body.[].", RECRUITMENT_FIELD_DESCRIPTORS)
                )
            )
    }

    @Test
    fun `지원 id로 지원 항목들을 position 순서대로 가져온다`() {
        every { recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId) } answers { recruitmentItems }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{id}/items",
                recruitmentId,
            )
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(recruitmentItems)
                    )
                )
            )
            .andDo(
                document(
                    "recruitments-findItemsById",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        RequestDocumentation.parameterWithName("id").description("모집 ID"),
                    ),
                    responseFields(
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("body.[]").description("모집 항목 목록"),
                    ).andWithPrefix("body.[].", RECRUITMENT_ITEM_FIELD_DESCRIPTORS)
                )
            )
    }

    @Test
    fun `지원과 지원 항목을 저장한다`() {
        every { recruitmentService.save(recruitmentData) } returns Unit

        mockMvc.perform(
            RestDocumentationRequestBuilders.post(
                "/api/recruitments"
            ).header("Content-Type", "application/json")
                .content(
                    objectMapper.writeValueAsString(recruitmentData)
                )
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "recruitments-save",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("모집 TITLE"),
                        fieldWithPath("startDateTime").type(JsonFieldType.STRING).description("모집 시작일"),
                        fieldWithPath("endDateTime").type(JsonFieldType.STRING).description("모집 마감일"),
                        fieldWithPath("recruitable").type(JsonFieldType.BOOLEAN).description("모집 가능 여부"),
                        fieldWithPath("hidden").type(JsonFieldType.BOOLEAN).description("HIDDEN"),
                        fieldWithPath("recruitmentItems").type(JsonFieldType.ARRAY).description("모집 항목 목록"),
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("모집 ID"),
                    ).andWithPrefix("recruitmentItems.[].", RECRUITMENT_ITEM_DATA_FIELD_DESCRIPTORS)
                )
            )
    }

    @Test
    fun `모집 id로 모집을 삭제한다`() {
        every { recruitmentService.deleteById(recruitmentId) } returns Unit

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete(
                "/api/recruitments/{id}",
                recruitmentId
            )
        ).andExpect(status().isOk)
    }

    @Test
    fun `모집 id로 모집을 가져온다`() {
        every { recruitmentService.getById(recruitmentId) } answers { recruitment }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{id}",
                recruitmentId
            )
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(recruitment)
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
        val RECRUITMENT_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("모집 TITLE"),
            fieldWithPath("recruitable").type(JsonFieldType.BOOLEAN).description("모집 가능 여부"),
            fieldWithPath("hidden").type(JsonFieldType.BOOLEAN).description("HIDDEN"),
            fieldWithPath("startDateTime").type(JsonFieldType.STRING).description("모집 시작일"),
            fieldWithPath("endDateTime").type(JsonFieldType.STRING).description("모집 마감일"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("모집 상태"),
        )

        val RECRUITMENT_ITEM_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("recruitmentId").type(JsonFieldType.NUMBER).description("모집 ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("모잡 TITLE"),
            fieldWithPath("position").type(JsonFieldType.NUMBER).description("모집 항목 POSITION"),
            fieldWithPath("maximumLength").type(JsonFieldType.NUMBER).description("모집 항목 최대 길이"),
            fieldWithPath("description").type(JsonFieldType.STRING).description("모집 설명"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("모집 항목 ID"),
        )

        val RECRUITMENT_ITEM_DATA_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("title").type(JsonFieldType.STRING).description("모잡 TITLE"),
            fieldWithPath("position").type(JsonFieldType.NUMBER).description("모집 항목 POSITION"),
            fieldWithPath("maximumLength").type(JsonFieldType.NUMBER).description("모집 항목 최대 길이"),
            fieldWithPath("description").type(JsonFieldType.STRING).description("모집 설명"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("모집 항목 ID"),
        )
    }
}
