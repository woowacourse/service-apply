package apply.ui.api

import apply.application.RecruitmentItemService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.createRecruitment
import apply.createRecruitmentData
import apply.createRecruitmentItem
import apply.createRecruitmentItemData
import apply.domain.term.Term
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [RecruitmentRestController::class]
)
internal class RecruitmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var recruitmentService: RecruitmentService

    @MockkBean
    private lateinit var recruitmentItemService: RecruitmentItemService

    private val recruitmentId = 1L
    private val recruitment = createRecruitment()
    private val recruitmentResponse = RecruitmentResponse(recruitment, Term.SINGLE)
    private val recruitmentItems = listOf(createRecruitmentItem())
    private val recruitmentData = createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData()))

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
                    pathParameters(
                        parameterWithName("id").description("모집 ID"),
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
        val recruitmentId = 1L
        every { recruitmentService.save(recruitmentData) } returns recruitmentId

        mockMvc.perform(
            RestDocumentationRequestBuilders.post(
                "/api/recruitments"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsString(recruitmentData))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string(HttpHeaders.LOCATION, "/api/recruitments/$recruitmentId"))
            .andDo(
                document(
                    "recruitments-save",
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("모집 TITLE"),
                        fieldWithPath("term.id").type(JsonFieldType.NUMBER).description("기수 ID"),
                        fieldWithPath("term.name").type(JsonFieldType.STRING).description("기수 이름"),
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
        every { recruitmentService.deleteById(recruitmentId) } just Runs

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete(
                "/api/recruitments/{id}",
                recruitmentId
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(recruitment)
                    )
                )
            )
    }

    @Test
    fun `종료되지 않은 모집 정보를 가져온다`() {
        every { recruitmentService.getNotEndedDataById(recruitmentId) } returns recruitmentData

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/{recruitmentId}/detail",
                recruitmentId
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(recruitmentData)
                    )
                )
            )
    }

    @Test
    fun `전체 모집 정보를 가져온다`() {
        every { recruitmentService.findAll() } returns listOf(recruitmentResponse)

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/recruitments/all"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        ).andExpect(status().isOk)
            .andExpect(
                content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(listOf(recruitmentResponse))
                    )
                )
            )
    }

    companion object {
        val RECRUITMENT_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("모집 TITLE"),
            fieldWithPath("term.id").type(JsonFieldType.NUMBER).description("기수 ID"),
            fieldWithPath("term.name").type(JsonFieldType.STRING).description("기수 이름"),
            fieldWithPath("recruitable").type(JsonFieldType.BOOLEAN).description("모집 가능 여부"),
            fieldWithPath("hidden").type(JsonFieldType.BOOLEAN).description("HIDDEN"),
            fieldWithPath("startDateTime").type(JsonFieldType.STRING).description("모집 시작일"),
            fieldWithPath("endDateTime").type(JsonFieldType.STRING).description("모집 마감일"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("모집 상태")
        )

        val RECRUITMENT_ITEM_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("recruitmentId").type(JsonFieldType.NUMBER).description("모집 ID"),
            fieldWithPath("title").type(JsonFieldType.STRING).description("모잡 TITLE"),
            fieldWithPath("position").type(JsonFieldType.NUMBER).description("모집 항목 POSITION"),
            fieldWithPath("maximumLength").type(JsonFieldType.NUMBER).description("모집 항목 최대 길이"),
            fieldWithPath("description").type(JsonFieldType.STRING).description("모집 설명"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("모집 항목 ID")
        )

        val RECRUITMENT_ITEM_DATA_FIELD_DESCRIPTORS = listOf(
            fieldWithPath("title").type(JsonFieldType.STRING).description("모집 TITLE"),
            fieldWithPath("position").type(JsonFieldType.NUMBER).description("모집 항목 POSITION"),
            fieldWithPath("maximumLength").type(JsonFieldType.NUMBER).description("모집 항목 최대 길이"),
            fieldWithPath("description").type(JsonFieldType.STRING).description("모집 설명"),
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("모집 항목 ID")
        )
    }
}
