package apply.ui.api

import apply.application.ApplicantAuthenticationService
import apply.application.ApplicantService
import apply.application.AuthenticateApplicantRequest
import apply.application.EditPasswordRequest
import apply.application.RegisterApplicantRequest
import apply.application.ResetPasswordRequest
import apply.application.mail.MailService
import apply.domain.applicant.ApplicantAuthenticationException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.security.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import support.createLocalDate
import support.test.TestEnvironment

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val RANDOM_PASSWORD = "nEw_p@ssw0rd"
private const val PASSWORD = "password"
private const val INVALID_PASSWORD = "invalid_password"

private fun RegisterApplicantRequest.withPlainPassword(password: String): Map<String, Any?> {
    return mapOf(
        "name" to name,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "gender" to gender,
        "birthday" to birthday,
        "password" to password
    )
}

private fun AuthenticateApplicantRequest.withPlainPassword(password: String): Map<String, Any?> {
    return mapOf("email" to email, "password" to password)
}

@WebMvcTest(
    controllers = [ApplicantRestController::class],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.security.*"]),
        ComponentScan.Filter(type = FilterType.REGEX, pattern = ["apply.config.*"])
    ]
)
@TestEnvironment
internal class ApplicantRestControllerTest(
    private val objectMapper: ObjectMapper
) {
    @MockBean
    private lateinit var applicantService: ApplicantService

    @MockBean
    private lateinit var applicantAuthenticationService: ApplicantAuthenticationService

    @MockBean
    private lateinit var mailService: MailService

    @MockBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var mockMvc: MockMvc

    private val applicantRequest = RegisterApplicantRequest(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = Password(PASSWORD)
    )

    private val applicantLoginRequest = AuthenticateApplicantRequest(
        email = applicantRequest.email,
        password = applicantRequest.password
    )

    private val applicantPasswordFindRequest = ResetPasswordRequest(
        name = applicantRequest.name,
        email = applicantRequest.email,
        birthday = applicantRequest.birthday
    )

    private val invalidApplicantRequest = applicantRequest.copy(password = Password(INVALID_PASSWORD))

    private val invalidApplicantLoginRequest = applicantLoginRequest.copy(password = Password(INVALID_PASSWORD))

    private val inValidApplicantPasswordFindRequest =
        applicantPasswordFindRequest.copy(birthday = createLocalDate(1995, 4, 4))

    private val validEditPasswordRequest = EditPasswordRequest(
        password = Password("password"),
        newPassword = Password("NEW_PASSWORD")
    )

    private val inValidEditPasswordRequest = validEditPasswordRequest.copy(password = Password("wrongPassword"))

    @BeforeEach
    internal fun setUp(webApplicationContext: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    @Test
    fun `유효한 지원자 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`() {
        given(applicantAuthenticationService.generateToken(applicantRequest))
            .willReturn(VALID_TOKEN)

        mockMvc.post("/api/applicants/register") {
            content = objectMapper.writeValueAsBytes(applicantRequest.withPlainPassword(PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(VALID_TOKEN))) }
        }
    }

    @Test
    fun `기존 지원자 정보와 일치하지 않는 지원자 생성 및 검증 요청에 응답으로 Unauthorized를 반환한다`() {
        given(
            applicantAuthenticationService.generateToken(invalidApplicantRequest)
        ).willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/register") {
            content = objectMapper.writeValueAsBytes(invalidApplicantRequest.withPlainPassword(INVALID_PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { json(objectMapper.writeValueAsString(ApiResponse.error("요청 정보가 기존 지원자 정보와 일치하지 않습니다"))) }
        }
    }

    @Test
    fun `올바른 지원자 로그인 요청에 응답으로 Token을 반환한다`() {
        given(
            applicantAuthenticationService.generateTokenByLogin(applicantLoginRequest)
        ).willReturn(VALID_TOKEN)

        mockMvc.post("/api/applicants/login") {
            content = objectMapper.writeValueAsBytes(applicantLoginRequest.withPlainPassword(PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(VALID_TOKEN))) }
        }
    }

    @Test
    fun `잘못된 지원자 로그인 요청에 응답으로 Unauthorized와 메시지를 반환한다`() {
        given(
            applicantAuthenticationService.generateTokenByLogin(invalidApplicantLoginRequest)
        ).willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/login") {
            content = objectMapper.writeValueAsBytes(invalidApplicantLoginRequest.withPlainPassword(INVALID_PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { json(objectMapper.writeValueAsString(ApiResponse.error("요청 정보가 기존 지원자 정보와 일치하지 않습니다"))) }
        }
    }

    @Test
    fun `올바른 비밀번호 찾기 요청에 응답으로 NoContent를 반환한다`() {
        given(
            applicantService.resetPassword(applicantPasswordFindRequest)
        ).willReturn(RANDOM_PASSWORD)

        willDoNothing().given(mailService).sendPasswordResetMail(applicantPasswordFindRequest, RANDOM_PASSWORD)

        mockMvc.post("/api/applicants/reset-password") {
            content = objectMapper.writeValueAsBytes(applicantPasswordFindRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `잘못된 비밀번호 찾기 요청에 응답으로 Unauthorized를 반환한다`() {
        given(
            applicantService.resetPassword(inValidApplicantPasswordFindRequest)
        ).willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/reset-password") {
            content = objectMapper.writeValueAsBytes(inValidApplicantPasswordFindRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    fun `올바른 비밀번호 변경 요청에 응답으로 NoContent를 반환한다`() {
        given(jwtTokenProvider.isValidToken("valid_token")).willReturn(true)
        given(jwtTokenProvider.getSubject("valid_token")).willReturn(applicantRequest.email)
        given(applicantService.getByEmail(applicantRequest.email)).willReturn(applicantRequest.toEntity())
        willDoNothing().given(applicantService).editPassword(applicantRequest.toEntity().id, validEditPasswordRequest)

        mockMvc.post("/api/applicants/edit-password") {
            content = objectMapper.writeValueAsBytes(validEditPasswordRequest)
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `잘못된 비밀번호 변경 요청에 응답으로 Unauthorized를 반환한다`() {
        given(jwtTokenProvider.isValidToken("valid_token")).willReturn(true)
        given(jwtTokenProvider.getSubject("valid_token")).willReturn(applicantRequest.email)
        given(applicantService.getByEmail(applicantRequest.email)).willReturn(applicantRequest.toEntity())
        given(applicantService.editPassword(applicantRequest.toEntity().id, inValidEditPasswordRequest))
            .willThrow(ApplicantAuthenticationException())

        mockMvc.post("/api/applicants/edit-password") {
            content = objectMapper.writeValueAsBytes(inValidEditPasswordRequest)
            contentType = MediaType.APPLICATION_JSON
            header(AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isNoContent }
        }
    }
}
