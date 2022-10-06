package apply.ui.api

import apply.application.AdministratorService
import apply.createAdministratorData
import apply.createAdministratorResponse
import apply.createAdministratorUpdateFormData
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import support.test.web.servlet.bearer

@WebMvcTest(AdministratorRestController::class)
class AdministratorRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var administratorService: AdministratorService

    @Test
    fun `관리자를 생성한다`() {
        val response = createAdministratorResponse(id = 1L)
        every { administratorService.save(any()) } returns response

        mockMvc.post("/api/administrators") {
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
            createAdministratorResponse(username = "admin1", id = 1L),
            createAdministratorResponse(username = "admin2", id = 2L),
            createAdministratorResponse(username = "admin3", id = 3L)
        )
        every { administratorService.findAll() } returns responses

        mockMvc.get("/api/administrators") {
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

        mockMvc.get("/api/administrators/{administratorId}", 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }
    }

    @Test
    fun `관리자를 수정한다`() {
        every { administratorService.update(any(), any()) } just Runs

        mockMvc.put("/api/administrators/{administratorId}", 1L) {
            jsonContent(createAdministratorUpdateFormData())
            bearer("valid_token")
        }.andExpect {
            status { isNoContent }
        }
    }
}
