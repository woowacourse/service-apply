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
import apply.ui.api.ApiResponse.Companion.success
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

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
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(listOf(recruitmentResponse)))) }
        }.andDo {
            handle(document("get/recruitments"))
        }
    }

    @Test
    fun `모집 id로 모집 항목들을 position 순서대로 가져온다`() {
        every { recruitmentItemService.findByRecruitmentIdOrderByPosition(recruitmentId) } returns recruitmentItems

        mockMvc.get("/api/recruitments/{id}/items", recruitmentId) {
            contentType = APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(recruitmentItems))) }
        }.andDo {
            handle(document("get/recruitments/items"))
        }
    }

    @Test
    fun `지원과 지원 항목을 저장한다`() {
        every { recruitmentService.save(recruitmentData) } returns recruitmentResponse

        mockMvc.post("/api/recruitments") {
            content = objectMapper.writeValueAsString(recruitmentData)
            contentType = APPLICATION_JSON
            bearer("valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(success(recruitmentResponse))) }
        }
    }

    @Test
    fun `모집 id로 모집을 삭제한다`() {
        every { recruitmentService.deleteById(recruitmentId) } just Runs

        mockMvc.delete("/api/recruitments/{id}", recruitmentId) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `모집 id로 모집을 가져온다`() {
        every { recruitmentService.getById(recruitmentId) } returns recruitmentResponse

        mockMvc.get("/api/recruitments/{id}", recruitmentId) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(recruitmentResponse))) }
        }
    }

    @Test
    fun `종료되지 않은 모집 정보를 가져온다`() {
        every { recruitmentService.getNotEndedDataById(recruitmentId) } returns recruitmentData

        mockMvc.get("/api/recruitments/{recruitmentId}/detail", recruitmentId) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(recruitmentData))) }
        }
    }

    @Test
    fun `전체 모집 정보를 가져온다`() {
        every { recruitmentService.findAll() } returns listOf(recruitmentResponse)

        mockMvc.get("/api/recruitments/all") {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(listOf(recruitmentResponse)))) }
        }
    }
}
