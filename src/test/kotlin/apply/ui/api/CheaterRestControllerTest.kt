package apply.ui.api

import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.createCheaterData
import apply.createUser
import apply.domain.cheater.Cheater
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(
    controllers = [CheaterRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class CheaterRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var cheaterService: CheaterService

    private val cheaterResponses: List<CheaterResponse> = listOf(
        CheaterResponse(Cheater(email = "loki@email.com"), createUser(name = "로키")),
        CheaterResponse(Cheater(email = "amazzi@email.com"), createUser(name = "아마찌"))
    )

    @Test
    fun `부정행위자를 조회한다`() {
        every { cheaterService.getById(any()) } returns cheaterResponses[0]

        mockMvc.get("/api/cheaters/{cheaterId}", 1L) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(cheaterResponses[0]))) }
        }
    }

    @Test
    fun `모든 부정행위자를 찾는다`() {
        every { cheaterService.findAll() } returns cheaterResponses

        mockMvc.get("/api/cheaters") {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(cheaterResponses))) }
        }
    }

    @Test
    fun `부정행위자를 추가한다`() {
        every { cheaterService.save(any()) } returns cheaterResponses[0]

        mockMvc.post("/api/cheaters") {
            content = objectMapper.writeValueAsString(createCheaterData())
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(cheaterResponses[0]))) }
        }
    }

    @Test
    fun `부정행위자를 삭제한다`() {
        every { cheaterService.deleteById(any()) } just Runs

        mockMvc.delete("/api/cheaters/{cheaterId}", 1L) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
