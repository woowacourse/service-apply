package apply.ui.api

import apply.application.AdministratorService
import apply.createAdministratorData
import apply.createAdministratorResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(AdministratorRestController::class)
internal class AdministratorRestControllerTest : RestControllerTest() {

    @MockkBean
    private lateinit var administratorService: AdministratorService

    @Test
    fun `관리자를 생성한다`() {
        val response = createAdministratorResponse(id = 1L)
        every { administratorService.save(any()) } returns response

        mockMvc.post("/api/admin/administrators") {
            jsonContent(createAdministratorData())
            bearer("valid_token")
        }.andExpect {
            status { isCreated }
            content { success(response) }
        }
    }

    @Test
    fun `모든 관리자를 조회한다`() {
        val responses = listOf(
            createAdministratorResponse(name = "adminA", username = "masterA", id = 1L),
            createAdministratorResponse(name = "adminB", username = "masterB", id = 2L),
            createAdministratorResponse(name = "adminC", username = "masterC", id = 3L)
        )
        every { administratorService.findAll() } returns responses

        mockMvc.get("/api/admin/administrators") {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(responses) }
        }
    }

    @Test
    fun `식별자로 관리자를 조회한다`() {
        val response = createAdministratorResponse()
        every { administratorService.findById(any()) } returns response

        mockMvc.get("/api/admin/administrators/1") {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }
    }
}
