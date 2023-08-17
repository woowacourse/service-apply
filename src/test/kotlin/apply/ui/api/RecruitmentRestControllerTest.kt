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
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(RecruitmentRestController::class)
class RecruitmentRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var recruitmentService: RecruitmentService

    @MockkBean
    private lateinit var recruitmentItemService: RecruitmentItemService

    @Test
    fun `공개된 모집 목록을 가져온다`() {
        val responses = listOf(RecruitmentResponse(createRecruitment(), Term.SINGLE))
        every { recruitmentService.findAllNotHidden() } returns responses

        mockMvc.get("/api/recruitments")
            .andExpect {
                status { isOk() }
                content { success(responses) }
            }.andDo {
                handle(document("recruitment-get"))
            }
    }

    @Test
    fun `모집 id로 모집 항목들을 position 순서대로 가져온다`() {
        val responses = listOf(createRecruitmentItem())
        every { recruitmentItemService.findByRecruitmentIdOrderByPosition(any()) } returns responses

        mockMvc.get("/api/recruitments/{id}/items", 1L)
            .andExpect {
                status { isOk() }
                content { success(responses) }
            }.andDo {
                handle(document("recruitment-item-list-get"))
            }
    }

    @Test
    fun `지원과 지원 항목을 저장한다`() {
        val response = RecruitmentResponse(createRecruitment(), Term.SINGLE)
        every { recruitmentService.save(any()) } returns response

        mockMvc.post("/api/recruitments") {
            jsonContent(createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData())))
            bearer("valid_token")
        }.andExpect {
            status { isCreated() }
            content { success(response) }
        }
    }

    @Test
    fun `모집 id로 모집을 삭제한다`() {
        every { recruitmentService.deleteById(any()) } just Runs

        mockMvc.delete("/api/recruitments/{id}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `모집 id로 모집을 가져온다`() {
        val response = RecruitmentResponse(createRecruitment(), Term.SINGLE)
        every { recruitmentService.getById(any()) } returns response

        mockMvc.get("/api/recruitments/{id}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `종료되지 않은 모집 정보를 가져온다`() {
        val response = createRecruitmentData(recruitmentItems = listOf(createRecruitmentItemData()))
        every { recruitmentService.getNotEndedDataById(any()) } returns response

        mockMvc.get("/api/recruitments/{recruitmentId}/detail", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `전체 모집 정보를 가져온다`() {
        val responses = listOf(RecruitmentResponse(createRecruitment(), Term.SINGLE))
        every { recruitmentService.findAll() } returns responses

        mockMvc.get("/api/recruitments/all") {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }
}
