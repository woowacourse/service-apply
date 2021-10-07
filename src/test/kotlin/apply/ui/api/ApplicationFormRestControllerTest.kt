package apply.ui.api

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.UserService
import apply.application.ApplicationFormResponse
import apply.application.ApplicationFormService
import apply.application.MyApplicationFormResponse
import apply.application.mail.MailService
import apply.createUser
import apply.createApplicationForm
import apply.createApplicationForms
import apply.domain.user.User
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.security.JwtTokenProvider
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import support.createLocalDate

@WebMvcTest(
    controllers = [ApplicationFormRestController::class]
)
internal class ApplicationFormRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var applicationFormService: ApplicationFormService

    @MockkBean
    private lateinit var applicantService: ApplicantService

    @MockkBean
    private lateinit var mailService: MailService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockkBean
    private lateinit var userService: UserService

    private val recruitmentId = 1L

    private val user = User(
        name = "홍길동1",
        email = "user_email@email.com",
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

    private val userKeyword = "아마찌"

    private val applicantAndFormResponses = listOf(
        ApplicantAndFormResponse(
            createUser(name = "로키"), false,
            createApplicationForms()[0]
        ),
        ApplicantAndFormResponse(
            createUser(name = userKeyword), false,
            createApplicationForms()[1]
        )
    )

    private val applicantAndFormFindByUserKeywordResponses = listOf(applicantAndFormResponses[1])

    @Test
    fun `올바른 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns user.email
        every { userService.getByEmail(user.email) } returns user
        every { applicationFormService.getApplicationForm(user.id, recruitmentId) } returns applicationFormResponse

        mockMvc.get("/api/application-forms") {
            param("recruitmentId", user.id.toString())
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicationFormResponse))) }
        }
    }

    @Test
    fun `내 지원서 요청에 정상적으로 응답한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns user.email
        every { userService.getByEmail(user.email) } returns user
        every { applicationFormService.getMyApplicationForms(user.id) } returns myApplicationFormResponses

        mockMvc.get("/api/application-forms/me") {
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
            applicantService.findAllByRecruitmentIdAndKeyword(
                recruitmentId,
                userKeyword
            )
        } returns applicantAndFormFindByUserKeywordResponses

        mockMvc.get("/api/recruitments/{recruitmentId}/application-forms", recruitmentId) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
            param("keyword", userKeyword)
        }
            .andExpect {
                status { isOk }
                content {
                    json(
                        objectMapper.writeValueAsString(
                            ApiResponse.success(
                                applicantAndFormFindByUserKeywordResponses
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
            applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId)
        } returns applicantAndFormResponses

        mockMvc.get(
            "/api/recruitments/{recruitmentId}/application-forms", recruitmentId
        ) {
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(applicantAndFormResponses))) }
        }
    }
}
