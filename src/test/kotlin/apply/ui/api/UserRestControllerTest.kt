package apply.ui.api

import apply.application.EditInformationRequest
import apply.application.ResetPasswordRequest
import apply.application.UserAuthenticationService
import apply.application.UserResponse
import apply.application.UserService
import apply.application.mail.MailService
import apply.createUser
import apply.domain.authenticationcode.AuthenticationCode
import apply.domain.member.Gender
import apply.domain.member.UnidentifiedMemberException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import support.createLocalDate
import support.test.web.servlet.bearer
import java.time.LocalDate

private const val PASSWORD = "password"
private const val INVALID_PASSWORD = "invalid_password"
private const val WRONG_PASSWORD = "wrong_password"
private const val NEW_PASSWORD = "new_password"

private fun createRegisterUserRequest(
    name: String = "회원",
    email: String = "test@email.com",
    phoneNumber: String = "010-0000-0000",
    gender: Gender = Gender.MALE,
    birthday: LocalDate = createLocalDate(1995, 2, 2),
    password: String = PASSWORD,
    confirmPassword: String = PASSWORD,
    authenticationCode: String = "3ea9fa6c"
): Map<String, Any> {
    return mapOf(
        "name" to name,
        "email" to email,
        "phoneNumber" to phoneNumber,
        "gender" to gender,
        "birthday" to birthday,
        "password" to password,
        "confirmPassword" to confirmPassword,
        "authenticationCode" to authenticationCode
    )
}

private fun createAuthenticateUserRequest(
    email: String = "test@email.com",
    password: String = PASSWORD
): Map<String, Any> {
    return mapOf(
        "email" to email,
        "password" to password
    )
}

private fun createEditPasswordRequest(
    oldPassword: String = PASSWORD,
    password: String = NEW_PASSWORD,
    confirmPassword: String = NEW_PASSWORD
): Map<String, String> {
    return mapOf(
        "oldPassword" to oldPassword,
        "password" to password,
        "confirmPassword" to confirmPassword
    )
}

@WebMvcTest(UserRestController::class)
class UserRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var userService: UserService

    @MockkBean
    private lateinit var userAuthenticationService: UserAuthenticationService

    @MockkBean
    private lateinit var mailService: MailService

    @Test
    fun `유효한 회원 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`() {
        val response = "valid_token"
        every { userAuthenticationService.generateTokenByRegister(any()) } returns response
        every { mailService.sendAuthenticationCodeMail(any(), any()) } just Runs

        mockMvc.post("/api/users/register") {
            jsonContent(createRegisterUserRequest())
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("user-register-post"))
        }
    }

    @Test
    fun `올바른 회원 로그인 요청에 응답으로 Token을 반환한다`() {
        val response = "valid_token"
        every { userAuthenticationService.generateTokenByLogin(any()) } returns response

        mockMvc.post("/api/users/login") {
            jsonContent(createAuthenticateUserRequest())
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("user-login-post"))
        }
    }

    @Test
    fun `잘못된 회원 로그인 요청에 응답으로 403 Forbidden을 반환한다`() {
        every { userAuthenticationService.generateTokenByLogin(any()) } throws UnidentifiedMemberException("사용자 정보가 일치하지 않습니다.")

        mockMvc.post("/api/users/login") {
            jsonContent(createAuthenticateUserRequest(password = INVALID_PASSWORD))
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            handle(document("user-login-post-forbidden"))
        }
    }

    @Test
    fun `올바른 비밀번호 찾기 요청에 응답으로 NoContent를 반환한다`() {
        every { userService.resetPassword(any()) } just Runs

        mockMvc.post("/api/users/reset-password") {
            jsonContent(ResetPasswordRequest("회원", "test@email.com", createLocalDate(1995, 2, 2)))
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("user-reset-password-post"))
        }
    }

    @Test
    fun `잘못된 비밀번호 찾기 요청에 응답으로 403 Forbidden을 반환한다`() {
        every { userService.resetPassword(any()) } throws UnidentifiedMemberException("사용자 정보가 일치하지 않습니다.")

        mockMvc.post("/api/users/reset-password") {
            jsonContent(ResetPasswordRequest("회원", "test@email.com", createLocalDate(1995, 4, 4)))
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            handle(document("user-reset-password-post-forbidden"))
        }
    }

    @Test
    fun `올바른 비밀번호 변경 요청에 응답으로 NoContent를 반환한다`() {
        every { userService.editPassword(any(), any()) } just Runs

        mockMvc.post("/api/users/edit-password") {
            jsonContent(createEditPasswordRequest())
            bearer("valid_token")
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("user-edit-password-post"))
        }
    }

    @Test
    fun `잘못된 비밀번호 변경 요청에 응답으로 403 Forbidden을 반환한다`() {
        every { userService.editPassword(any(), any()) } throws UnidentifiedMemberException("기존 비밀번호가 일치하지 않습니다.")

        mockMvc.post("/api/users/edit-password") {
            jsonContent(createEditPasswordRequest(oldPassword = WRONG_PASSWORD))
            bearer("valid_token")
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            handle(document("user-edit-password-post-forbidden"))
        }
    }

    @Test
    fun `이메일 인증 코드 요청에 응답으로 NoContent를 반환한다`() {
        val authenticationCode = AuthenticationCode("authentication-code@email.com")
        every { userAuthenticationService.generateAuthenticationCode(any()) } returns authenticationCode.code
        every { mailService.sendAuthenticationCodeMail(any(), any()) } just Runs

        mockMvc.post("/api/users/authentication-code") {
            param("email", authenticationCode.email)
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("user-authentication-code-post"))
        }
    }

    @Test
    fun `이메일 인증 요청에 응답으로 NoContent를 반환한다`() {
        every { userAuthenticationService.authenticateEmail(any(), any()) } just Runs

        mockMvc.post("/api/users/authenticate-email") {
            param("email", "test@email.com")
            param("authenticationCode", "code")
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("user-authenticate-email-post"))
        }
    }

    @Test
    fun `키워드(이름 or 이메일)로 회원들을 조회한다`() {
        val responses = listOf(UserResponse(createUser("아마찌")))
        every { userService.findAllByKeyword(any()) } returns responses

        mockMvc.get("/api/users") {
            bearer("valid_token")
            param("keyword", "아마찌")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `회원이 자신의 정보를 조회한다`() {
        val response = UserResponse(createUser())
        every { userService.getInformation(any()) } returns response

        mockMvc.get("/api/users/me") {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("user-me-get"))
        }
    }

    @Test
    fun `회원이 정보를 변경한다`() {
        every { userService.editInformation(any(), any()) } just Runs

        mockMvc.patch("/api/users/information") {
            jsonContent(EditInformationRequest("010-9999-9999"))
            bearer("valid_token")
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("user-information-patch"))
        }
    }
}
