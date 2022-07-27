package apply.ui.api

import apply.application.TermData
import apply.application.TermResponse
import apply.application.TermService
import apply.domain.term.Term
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
    controllers = [TermRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"])
    ]
)
internal class TermRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var termService: TermService

    @Test
    fun `기수를 생성한다`() {
        val response = TermResponse(Term("4기"))
        every { termService.save(TermData("4기")) } returns response

        mockMvc.post("/api/terms") {
            content = objectMapper.writeValueAsString(TermData("4기"))
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(response))) }
        }
    }

    @Test
    fun `기수를 조회한다`() {
        val response = TermResponse(1L, "4기")
        every { termService.getById(any()) } returns response

        mockMvc.get("/api/terms/{termId}", 1L) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(response))) }
        }
    }

    @Test
    fun `전체 기수를 조회한다`() {
        val responses = listOf(Term.SINGLE, Term("1기"), Term("2기")).map { TermResponse(it) }
        every { termService.findAll() } returns responses

        mockMvc.get("/api/terms") {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(responses))) }
        }
    }

    @Test
    fun `기수를 삭제한다`() {
        every { termService.deleteById(any()) } just Runs

        mockMvc.delete("/api/terms/{termId}", 1L) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
