package apply.ui.api

import apply.application.TermData
import apply.application.TermResponse
import apply.application.TermService
import apply.application.UserService
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
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var termService: TermService

    @Test
    fun `기수를 생성한다`() {
        val termId = 1L
        every { termService.save(TermData("3기")) } returns termId

        mockMvc.post(
            "/api/terms"
        ) {
            content = objectMapper.writeValueAsString(TermData("3기"))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated }
            header { string(HttpHeaders.LOCATION, "/api/terms/$termId") }
        }
    }

    @Test
    fun `전체 기수를 조회한다`() {
        val terms = listOf(Term.SINGLE, Term("1기"), Term("2기")).map { TermResponse(it) }
        every { termService.findAll() } returns terms

        mockMvc.get(
            "/api/terms"
        ).andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(terms))) }
        }
    }

    @Test
    fun `기수를 삭제한다`() {
        val termId = 1L
        every { termService.deleteById(termId) } just Runs

        mockMvc.delete(
            "/api/terms/{termId}", termId
        ).andExpect {
            status { isOk }
        }
    }
}
