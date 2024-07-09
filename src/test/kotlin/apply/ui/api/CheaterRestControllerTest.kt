package apply.ui.api

import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.createCheaterData
import apply.createMember
import apply.domain.cheater.Cheater
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

@WebMvcTest(CheaterRestController::class)
class CheaterRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var cheaterService: CheaterService

    @Test
    fun `부정행위자를 조회한다`() {
        val response = CheaterResponse(Cheater(email = "loki@email.com"), createMember(name = "로키"))
        every { cheaterService.getById(any()) } returns response

        mockMvc.get("/api/cheaters/{cheaterId}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `모든 부정행위자를 찾는다`() {
        val responses = listOf(
            CheaterResponse(Cheater(email = "loki@email.com"), createMember(name = "로키")),
            CheaterResponse(Cheater(email = "amazzi@email.com"), createMember(name = "아마찌"))
        )
        every { cheaterService.findAll() } returns responses

        mockMvc.get("/api/cheaters") {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `부정행위자를 추가한다`() {
        val response = CheaterResponse(Cheater(email = "loki@email.com"), createMember(name = "로키"))
        every { cheaterService.save(any()) } returns response

        mockMvc.post("/api/cheaters") {
            jsonContent(createCheaterData())
            bearer("valid_token")
        }.andExpect {
            status { isCreated() }
            content { success(response) }
        }
    }

    @Test
    fun `부정행위자를 삭제한다`() {
        every { cheaterService.deleteById(any()) } just Runs

        mockMvc.delete("/api/cheaters/{cheaterId}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
        }
    }
}
