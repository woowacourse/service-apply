package apply.ui.api

import apply.application.MailHistoryService
import apply.createMailData
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
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
    private lateinit var mailHistoryService: MailHistoryService

    @Test
    fun `이메일 이력을 저장한다`() {
        val mailHistoryId = 1L
        every { mailHistoryService.save(any()) } returns mailHistoryId

        mockMvc.post("/api/mail-history") {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createMailData())
        }.andExpect {
            status { isCreated }
            header { string(HttpHeaders.LOCATION, "/api/mail-history/$mailHistoryId") }
        }
    }

    @Test
    fun `이메일 내역을 단일 조회한다`() {
        val mailData = createMailData()
        every { mailHistoryService.getById(any()) } returns mailData

        mockMvc.get("/api/mail-history/{mailHistoryId}", mailData.id) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(mailData))) }
        }
    }

    @Test
    fun `모든 이메일 내역을 조회한다`() {
        val mailDataValues = listOf(createMailData(), createMailData())
        every { mailHistoryService.findAll() } returns mailDataValues

        mockMvc.get("/api/mail-history") {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(mailDataValues))) }
        }
    }
}
