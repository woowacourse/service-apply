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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

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
        val termResponse = TermResponse(1L, "3기")
        every { termService.save(TermData("3기")) } returns termResponse

        mockMvc.post(
            "/api/terms"
        ) {
            content = objectMapper.writeValueAsString(TermData("3기"))
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isCreated }
            header { string(HttpHeaders.LOCATION, "/api/terms/${termResponse.id}") }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(termResponse))) }
        }
    }

    @Test
    fun `기수 id로 기수를 조회한다`() {
        val term = Term("1기", 1L)
        every { termService.getById(term.id) } answers { term }

        mockMvc.perform(
            RestDocumentationRequestBuilders.get(
                "/api/terms/{id}",
                term.id
            )
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(
                        ApiResponse.success(term)
                    )
                )
            )
    }

    @Test
    fun `전체 기수를 조회한다`() {
        val terms = listOf(Term.SINGLE, Term("1기"), Term("2기")).map { TermResponse(it) }
        every { termService.findAll() } returns terms

        mockMvc.get(
            "/api/terms"
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
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
        ) {
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
        }
    }
}
