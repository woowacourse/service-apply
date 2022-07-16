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
import support.createLocalDateTime

@WebMvcTest(
    controllers = [CheaterRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class CheaterRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var cheaterService: CheaterService

    private val cheaterResponses = listOf(
        CheaterResponse(
            Cheater(email = "loki@email.com", createdDateTime = createLocalDateTime(2021, 10, 9, 10, 0, 0, 0)),
            createUser(name = "로키")
        ),
        CheaterResponse(
            Cheater(email = "amazzi@email.com", createdDateTime = createLocalDateTime(2021, 10, 10, 10, 0, 0, 0)),
            createUser(name = "아마찌")
        )
    )
    private val cheatedUser = createUser(id = 1L, name = "로키")

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
        val cheaterData = createCheaterData()
        val cheaterId = 1L
        every { cheaterService.save(cheaterData) } returns cheaterId

        mockMvc.post("/api/cheaters") {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(cheaterData)
        }.andExpect {
            status { isCreated }
            header { string(HttpHeaders.LOCATION, "/api/cheaters/$cheaterId") }
        }
    }

    @Test
    fun `부정행위자를 삭제한다`() {
        every { cheaterService.deleteById(cheatedUser.id) } just Runs

        mockMvc.delete("/api/cheaters/{cheaterId}", cheatedUser.id) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
