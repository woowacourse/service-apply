package apply.ui.api

import apply.application.ApplicantService
import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantVerifyInformation
import apply.domain.applicant.Gender
import apply.domain.applicant.exception.ApplicantValidateException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDate

private const val VALID_TOKEN = "SOME_VALID_TOKEN"

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WebMvcTest(controllers = [ApplicantRestController::class])
internal class ApplicantRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var applicantService: ApplicantService

    private lateinit var mockMvc: MockMvc

    private val applicantRequest = ApplicantInformation(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = "password"
    )

    private val applicantLoginRequest = ApplicantVerifyInformation(
        name = "지원자",
        email = "test@email.com",
        birthday = createLocalDate(1995, 2, 2),
        password = "password"
    )

    private val invalidApplicantRequest = applicantRequest.copy(password = "invalid_password")

    private val invalidApplicantLoginRequest = applicantLoginRequest.copy(password = "invalid_password")

    @BeforeEach
    internal fun setUp(webApplicationContext: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    @Test
    fun `유효한 지원자 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`() {
        given(applicantService.generateToken(applicantRequest))
            .willReturn(VALID_TOKEN)

        mockMvc.post("/api/applicants/register") {
            content = objectMapper.writeValueAsBytes(applicantRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { string(VALID_TOKEN) }
        }
    }

    @Test
    fun `기존 지원자 정보와 일치하지 않는 지원자 생성 및 검증 요청에 대하여 unauthorized 응답을 받는다`() {
        given(
            applicantService.generateToken(invalidApplicantRequest)
        ).willThrow(ApplicantValidateException())

        mockMvc.post("/api/applicants/register") {
            content = objectMapper.writeValueAsBytes(invalidApplicantRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { string("잘못된 요청입니다") }
        }
    }

    @Test
    fun `지원자 로그인 요청에 응답으로 Token을 반환한다`() {
        given(
            applicantService.generateTokenByLogin(applicantLoginRequest)
        ).willReturn(VALID_TOKEN)

        mockMvc.post("/api/applicants/login") {
            content = objectMapper.writeValueAsBytes(applicantLoginRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { string(VALID_TOKEN) }
        }
    }

    @Test
    fun `잘못된 지원자 로그인 요청에 응답으로 Bad Request와 메시지를 반환한다`() {
        given(
            applicantService.generateTokenByLogin(invalidApplicantLoginRequest)
        ).willThrow(ApplicantValidateException())

        mockMvc.post("/api/applicants/login") {
            content = objectMapper.writeValueAsBytes(invalidApplicantLoginRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
            content { string("등록된 지원자를 찾을 수 없습니다") }
        }
    }
}
