package apply.ui.api

import apply.application.MailHistoryService
import apply.application.UserService
import apply.application.mail.MailData
import apply.createMailData
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [MailHistoryRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
class MailHistoryRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var mailHistoryService: MailHistoryService

    @Test
    fun `이메일 이력을 저장한다`() {
        every { mailHistoryService.save(any()) } just Runs

        mockMvc.post("/api/mail-history") {
            content = objectMapper.writeValueAsString(createMailData())
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
        }
    }

    @Test
    fun `이메일 내역을 단일 조회한다`() {
        val mailData: MailData = createMailData()
        every { mailHistoryService.getById(any()) } returns mailData

        mockMvc.get("/api/mail-history/{mailHistoryId}", mailData.id)
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(mailData))) }
            }
    }

    @Test
    fun `모든 이메일 내역을 조회한다`() {
        val mailDataValues: List<MailData> = listOf(createMailData(), createMailData())
        every { mailHistoryService.findAll() } returns mailDataValues

        mockMvc.get("/api/mail-history")
            .andExpect {
                status { isOk }
                content { json(objectMapper.writeValueAsString(ApiResponse.success(mailDataValues))) }
            }
    }
}
