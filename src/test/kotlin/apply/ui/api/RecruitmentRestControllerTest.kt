package apply.ui.api

import apply.application.RecruitmentData
import apply.application.RecruitmentItemService
import apply.application.RecruitmentResponse
import apply.application.RecruitmentService
import apply.createRecruitment
import apply.createRecruitmentData
import apply.createRecruitmentItem
import apply.createRecruitmentItemData
import apply.domain.recruitment.Recruitment
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.term.Term
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [RecruitmentRestController::class]
)
class RecruitmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var recruitmentService: RecruitmentService

    @MockkBean
    private lateinit var recruitmentItemService: RecruitmentItemService

    private val recruitmentId: Long = 1L
    private val recruitment: Recruitment = createRecruitment()
    private val recruitmentResponse: RecruitmentResponse = RecruitmentResponse(recruitment, Term.SINGLE)
    private val recruitmentItems: List<RecruitmentItem> = listOf(createRecruitmentItem())
    private val recruitmentData: RecruitmentData = createRecruitmentData(
        recruitmentItems = listOf(createRecruitmentItemData())
    )

    @Test
    fun `공개된 모집 목록을 가져온다`() {
        every { recruitmentService.findAllNotHidden() } returns listOf(recruitmentResponse)

        mockMvc.get("/api/recruitments") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(listOf(recruitmentResponse)))) }
        }.andDo {
            handle(document("recruitment-findAllNotHidden"))
        }
    }

    @Test
    fun `모집 id로 모집 항목들을 position 순서대로 가져온다`() {
        every { recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId) } returns recruitmentItems

        mockMvc.get("/api/recruitments/{id}/items", recruitmentId) {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(recruitmentItems))) }
        }.andDo {
            handle(document("recruitment-findItemsById"))
        }
    }

    @Test
    fun `지원과 지원 항목을 저장한다`() {
        every { recruitmentService.save(recruitmentData) } returns recruitmentResponse

        mockMvc.perform(
            RestDocumentationRequestBuilders.post(
                "/api/recruitments"
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .content(objectMapper.writeValueAsString(recruitmentData))
        ).andExpect {
            status().isCreated
            content().json(objectMapper.writeValueAsString(ApiResponse.success(recruitment)))
        }
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
        every { recruitmentService.getById(recruitmentId) } answers { recruitmentResponse }

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
                        ApiResponse.success(recruitmentResponse)
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
