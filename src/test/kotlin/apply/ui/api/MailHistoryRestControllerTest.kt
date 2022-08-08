package apply.ui.api

import apply.application.MailHistoryService
import apply.createMailData
import apply.ui.api.ApiResponse.Companion.success
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.test.web.servlet.get
import support.test.web.servlet.bearer

@WebMvcTest(
    controllers = [MailHistoryRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
class MailHistoryRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var mailHistoryService: MailHistoryService

    @Test
    fun `이메일 내역을 단일 조회한다`() {
        val mailData = createMailData()
        every { mailHistoryService.getById(any()) } returns mailData

        mockMvc.get("/api/mail-history/{mailHistoryId}", mailData.id) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(mailData))) }
        }
    }

    @Test
    fun `모든 이메일 내역을 조회한다`() {
        val mailDataValues = listOf(createMailData(), createMailData())
        every { mailHistoryService.findAll() } returns mailDataValues

        mockMvc.get("/api/mail-history") {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(success(mailDataValues))) }
        }
    }
}
