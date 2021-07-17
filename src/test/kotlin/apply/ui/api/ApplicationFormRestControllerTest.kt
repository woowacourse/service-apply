package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.MyApplicationFormResponse
import apply.application.mail.MailService
import apply.createApplicant
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
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDate

@WebMvcTest(
    controllers = [ApplicationFormRestController::class]
)
internal class ApplicationFormRestControllerTest(
    private val objectMapper: ObjectMapper
) : RestControllerTest(objectMapper) {
    @MockkBean
    private lateinit var applicationFormService: ApplicationFormService

    @MockkBean
    private lateinit var mailService: MailService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var applicantService: ApplicantService

    private val recruitmentId = 1L

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

    private val applicantKeyword = "아마찌"

    private val applicantAndFormResponses = listOf(
        ApplicantAndFormResponse(
            createApplicant(name = "로키"), false,
            createApplicationForms()[0]
        ),
        ApplicantAndFormResponse(
            createApplicant(name = applicantKeyword), false,
            createApplicationForms()[1]
        )
    )

    private val applicantAndFormFindByApplicantKeywordResponses = listOf(applicantAndFormResponses[1])

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
        every { applicationFormService.getApplicationForm(applicant.id, recruitmentId) } returns applicationFormResponse

        mockMvc.get("/api/recruitments/{recruitmentId}/application-forms", recruitmentId) {
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

        mockMvc.get("/api/recruitments/{recruitmentId}/application-forms/me", recruitmentId) {
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(myApplicationFormResponses))) }
        }
    }

    @Test
    fun `특정 모집 id와 지원자에 대한 키워드(이름 or 이메일)로 지원서 정보들을 조회한다`() {
        val recruitmentId = applicantAndFormResponses[0].applicationForm.recruitmentId

        every {
            applicantService.findAllByRecruitmentIdAndSubmittedTrueAndKeyword(
                recruitmentId,
                applicantKeyword
            )
        } returns applicantAndFormFindByApplicantKeywordResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/application-forms/applicants",
            recruitmentId
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
            param("keyword", applicantKeyword)
        }
            .andExpect {
                status { isOk }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            ApiResponse.success(
                                applicantAndFormFindByApplicantKeywordResponses
                            )
                        )
                    )
                }
            }
    }

    @Test
    fun `특정 모집 id에 지원완료한 지원서 정보들을 조회한다`() {
        val recruitmentId = applicantAndFormResponses[0].applicationForm.recruitmentId

        every {
            applicantService.findAllByRecruitmentIdAndSubmittedTrueAndKeyword(
                recruitmentId,
                null
            )
        } returns applicantAndFormResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/application-forms/applicants",
            recruitmentId
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicantAndFormResponses))) }
        }
    }
}
