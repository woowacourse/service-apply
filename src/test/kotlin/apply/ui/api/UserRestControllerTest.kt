package apply.ui.api

import apply.application.AuthenticateUserRequest
import apply.application.EditPasswordRequest
import apply.application.RegisterUserRequest
import apply.application.ResetPasswordRequest
import apply.application.UserAuthenticationService
import apply.application.UserService
import apply.application.mail.MailService
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.UserAuthenticationException
import apply.security.JwtTokenProvider
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import support.createLocalDate
import support.test.TestEnvironment

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val RANDOM_PASSWORD = "nEw_p@ssw0rd"
private const val PASSWORD = "password"
private const val INVALID_PASSWORD = "invalid_password"
private const val WRONG_PASSWORD = "wrongPassword"
private const val NEW_PASSWORD = "NEW_PASSWORD"
@WebMvcTest(
    controllers = [UserRestController::class]
)
@TestEnvironment
class UserRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var userAuthenticationService: UserAuthenticationService

    @MockkBean
    private lateinit var mailService: MailService

    @MockkBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private val userRequest = RegisterUserRequest(
        name = "회원",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = Password(PASSWORD)
    )

    private val userLoginRequest = AuthenticateUserRequest(
        email = userRequest.email,
        password = userRequest.password
    )

    private val applicantPasswordFindRequest = ResetPasswordRequest(
        name = userRequest.name,
        email = userRequest.email,
        birthday = userRequest.birthday
    )

    private val invalidApplicantRequest = userRequest.copy(password = Password(INVALID_PASSWORD))

    private val invalidApplicantLoginRequest = userLoginRequest.copy(password = Password(INVALID_PASSWORD))

    private val inValidApplicantPasswordFindRequest =
        applicantPasswordFindRequest.copy(birthday = createLocalDate(1995, 4, 4))

    private val validEditPasswordRequest = EditPasswordRequest(
        password = Password(PASSWORD),
        newPassword = Password(NEW_PASSWORD)
    )

    private val inValidEditPasswordRequest = validEditPasswordRequest.copy(password = Password(WRONG_PASSWORD))

    @Test
    fun `유효한 회원 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`() {
        every { userAuthenticationService.generateToken(userRequest) } returns VALID_TOKEN
        every { mailService.sendAuthenticationMail(userRequest, any()) } just Runs
        every { userService.getByEmail(userRequest.email) } returns userRequest.toEntity()

        mockMvc.post("/api/users/register") {
            content = objectMapper.writeValueAsBytes(userRequest.withPlainPassword(PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(VALID_TOKEN))) }
        }
    }

    @Test
    fun `기존 회원 정보와 일치하지 않는 회원 생성 및 검증 요청에 응답으로 Unauthorized를 반환한다`() {
        every {
            userAuthenticationService.generateToken(invalidApplicantRequest)
        } throws UserAuthenticationException()

        mockMvc.post("/api/users/register") {
            content = objectMapper.writeValueAsBytes(invalidApplicantRequest.withPlainPassword(INVALID_PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { json(objectMapper.writeValueAsString(ApiResponse.error("요청 정보가 기존 회원 정보와 일치하지 않습니다"))) }
        }
    }

    @Test
    fun `올바른 회원 로그인 요청에 응답으로 Token을 반환한다`() {
        every {
            userAuthenticationService.generateTokenByLogin(userLoginRequest)
        } returns VALID_TOKEN

        mockMvc.post("/api/users/login") {
            content = objectMapper.writeValueAsBytes(userLoginRequest.withPlainPassword(PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content { json(objectMapper.writeValueAsString(ApiResponse.success(VALID_TOKEN))) }
        }
    }

    @Test
    fun `잘못된 회원 로그인 요청에 응답으로 Unauthorized와 메시지를 반환한다`() {
        every {
            userAuthenticationService.generateTokenByLogin(invalidApplicantLoginRequest)
        } throws UserAuthenticationException()

        mockMvc.post("/api/users/login") {
            content = objectMapper.writeValueAsBytes(invalidApplicantLoginRequest.withPlainPassword(INVALID_PASSWORD))
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
            content { json(objectMapper.writeValueAsString(ApiResponse.error("요청 정보가 기존 회원 정보와 일치하지 않습니다"))) }
        }
    }

    @Test
    fun `올바른 비밀번호 찾기 요청에 응답으로 NoContent를 반환한다`() {
        every {
            userService.resetPassword(applicantPasswordFindRequest)
        } returns RANDOM_PASSWORD

        every { mailService.sendPasswordResetMail(applicantPasswordFindRequest, RANDOM_PASSWORD) } just Runs

        mockMvc.post("/api/users/reset-password") {
            content = objectMapper.writeValueAsBytes(applicantPasswordFindRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `잘못된 비밀번호 찾기 요청에 응답으로 Unauthorized를 반환한다`() {
        every {
            userService.resetPassword(inValidApplicantPasswordFindRequest)
        } throws UserAuthenticationException()

        mockMvc.post("/api/users/reset-password") {
            content = objectMapper.writeValueAsBytes(inValidApplicantPasswordFindRequest)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    fun `올바른 비밀번호 변경 요청에 응답으로 NoContent를 반환한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns userRequest.email
        every { userService.getByEmail(userRequest.email) } returns userRequest.toEntity()
        every { userService.editPassword(any(), eq(validEditPasswordRequest)) } just Runs

        val actualValidEditPasswordRequest = createValidEditPasswordRequest()

        mockMvc.post("/api/users/edit-password") {
            content = objectMapper.writeValueAsBytes(actualValidEditPasswordRequest)
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `잘못된 비밀번호 변경 요청에 응답으로 Unauthorized를 반환한다`() {
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns userRequest.email
        every { userService.getByEmail(userRequest.email) } returns userRequest.toEntity()
        every {
            userService.editPassword(any(), eq(inValidEditPasswordRequest))
        } throws UserAuthenticationException()

        val actualInValidEditPasswordRequest = createInValidEditPasswordRequest()

        mockMvc.post("/api/users/edit-password") {
            content = objectMapper.writeValueAsBytes(actualInValidEditPasswordRequest)
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
        }.andExpect {
            status { isUnauthorized }
        }
    }

    @Test
    fun `이메일 인증 요청에 응답으로 NoContent를 반환한다`() {
        every { userAuthenticationService.authenticateEmail(userRequest.email, any()) } just Runs

        mockMvc.post("/api/users/authenticate-email") {
            param("email", userRequest.email)
            param("authenticateCode", "code")
        }.andExpect {
            status { isNoContent }
        }
    }

    private fun createValidEditPasswordRequest(): Map<String, String> {
        return mapOf(
            "password" to PASSWORD,
            "newPassword" to NEW_PASSWORD
        )
    }

    private fun createInValidEditPasswordRequest(): Map<String, String> {
        return mapOf(
            "password" to WRONG_PASSWORD,
            "newPassword" to NEW_PASSWORD
        )
    }
}

private fun RegisterUserRequest.withPlainPassword(password: String): Map<String, Any?> {
    return mapOf(
        "name" to name,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "gender" to gender,
        "birthday" to birthday,
        "password" to password
    )
}

private fun AuthenticateUserRequest.withPlainPassword(password: String): Map<String, Any?> {
    return mapOf("email" to email, "password" to password)
}
