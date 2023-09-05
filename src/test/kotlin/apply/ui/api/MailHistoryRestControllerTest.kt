package apply.ui.api

import apply.application.MailHistoryService
import apply.createMailData
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import support.test.web.servlet.bearer

@WebMvcTest(MailHistoryRestController::class)
class MailHistoryRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var mailHistoryService: MailHistoryService

    @Test
    fun `이메일 내역을 단일 조회한다`() {
        val response = createMailData()
        every { mailHistoryService.getById(any()) } returns response

        mockMvc.get("/api/mail-history/{mailHistoryId}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `모든 이메일 내역을 조회한다`() {
        val responses = listOf(createMailData(id = 1L), createMailData(id = 2L))
        every { mailHistoryService.findAll() } returns responses

        mockMvc.get("/api/mail-history") {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }
}
