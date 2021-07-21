package apply.ui.api

import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.MyApplicationFormResponse
import apply.application.mail.MailService
import apply.createApplicationForm
import apply.createApplicationForms
import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.security.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDate
import support.test.TestEnvironment

@WebMvcTest(
    controllers = [ApplicationFormRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@TestEnvironment
internal class ApplicationFormRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockkBean
    private lateinit var applicationFormService: ApplicationFormService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var mailService: MailService

    @MockkBean
    private lateinit var applicantService: ApplicantService
    private lateinit var mockMvc: MockMvc

    private val applicant = Applicant(
        name = "홍길동1",
        email = "applicant_email@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password"),
        id = 1L
    )

    private val applicationFormResponse = ApplicationFormResponse(
        createApplicationForm()
    )

    private val myApplicationFormResponses = createApplicationForms().map(::MyApplicationFormResponse)

    @BeforeEach
    internal fun setUp(webApplicationContext: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    @Test
    fun `올바른 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns applicant.email
        every { applicantService.getByEmail(applicant.email) } returns applicant
        every { applicationFormService.getApplicationForm(applicant.id, 2L) } returns applicationFormResponse

        mockMvc.get("/api/application-forms") {
            param("recruitmentId", "2")
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicationFormResponse))) }
        }
    }

    @Test
    fun `내 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns applicant.email
        every { applicantService.getByEmail(applicant.email) } returns applicant
        every { applicationFormService.getMyApplicationForms(applicant.id) } returns myApplicationFormResponses

        mockMvc.get("/api/application-forms/me") {
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(myApplicationFormResponses))) }
        }
    }
}
