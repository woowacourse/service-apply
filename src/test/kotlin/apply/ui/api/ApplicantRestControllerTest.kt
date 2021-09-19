package apply.ui.api

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.createApplicant
import apply.security.JwtTokenProvider
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [ApplicantRestController::class]
)
@TestEnvironment
internal class ApplicantRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val applicantKeyword = "아마찌"

    private val applicantResponses = listOf(
        ApplicantResponse(createApplicant("아마찌")),
        ApplicantResponse(createApplicant("로키"))
    )

    @Test
    fun `키워드(이름 or 이메일)로 지원자들을 조회한다`() {
        every { applicantService.findAllByKeyword(applicantKeyword) } returns applicantResponses

        mockMvc.get(
            "/api/applicants",
            applicantKeyword
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
            param("keyword", applicantKeyword)
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicantResponses))) }
        }
    }
}
