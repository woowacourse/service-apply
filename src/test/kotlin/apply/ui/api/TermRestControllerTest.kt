package apply.ui.api

import apply.application.TermData
import apply.application.TermResponse
import apply.application.TermService
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

@WebMvcTest(TermRestController::class)
class TermRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var termService: TermService

    @Test
    fun `기수를 생성한다`() {
        val response = TermResponse(1L, "4기")
        every { termService.save(any()) } returns response

        mockMvc.post("/api/terms") {
            jsonContent(TermData("4기"))
            bearer("valid_token")
        }.andExpect {
            status { isCreated }
            content { success(response) }
        }
    }

    @Test
    fun `기수를 조회한다`() {
        val response = TermResponse(1L, "4기")
        every { termService.getById(any()) } returns response

        mockMvc.get("/api/terms/{termId}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }
    }

    @Test
    fun `전체 기수를 조회한다`() {
        val responses = listOf(TermResponse(1L, "1기"), TermResponse(2L, "2기"), TermResponse(3L, "3기"))
        every { termService.findAll() } returns responses

        mockMvc.get("/api/terms") {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(responses) }
        }
    }

    @Test
    fun `기수를 삭제한다`() {
        every { termService.deleteById(any()) } just Runs

        mockMvc.delete("/api/terms/{termId}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
