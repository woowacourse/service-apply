package apply.ui.api

import apply.application.EditInformationRequest
import apply.application.MemberAuthenticationService
import apply.application.MemberResponse
import apply.application.MemberService
import apply.application.ResetPasswordRequest
import apply.application.mail.MailService
import apply.createMember
import apply.domain.authenticationcode.AuthenticationCode
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

private fun createRegisterMemberRequest(
    email: String = "test@email.com",
    password: String = PASSWORD,
    confirmPassword: String = PASSWORD,
    name: String = "회원",
    birthday: LocalDate = createLocalDate(1995, 2, 2),
    phoneNumber: String = "010-0000-0000",
    githubUsername: String = "jaeyeonling",
    authenticationCode: String = "3ea9fa6c",
): Map<String, Any> {
    return mapOf(
        "email" to email,
        "password" to password,
        "confirmPassword" to confirmPassword,
        "name" to name,
        "birthday" to birthday,
        "phoneNumber" to phoneNumber,
        "githubUsername" to githubUsername,
        "authenticationCode" to authenticationCode,
    )
}

private fun createAuthenticateMemberRequest(
    email: String = "test@email.com",
    password: String = PASSWORD,
): Map<String, Any> {
    return mapOf(
        "email" to email,
        "password" to password,
    )
}

private fun createEditPasswordRequest(
    oldPassword: String = PASSWORD,
    password: String = NEW_PASSWORD,
    confirmPassword: String = NEW_PASSWORD,
): Map<String, String> {
    return mapOf(
        "oldPassword" to oldPassword,
        "password" to password,
        "confirmPassword" to confirmPassword,
    )
}

@WebMvcTest(MemberRestController::class)
class MemberRestControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var memberService: MemberService

    @MockkBean
    private lateinit var memberAuthenticationService: MemberAuthenticationService

    @MockkBean
    private lateinit var mailService: MailService

    @Test
    fun `유효한 회원 생성 및 검증 요청에 대하여 응답으로 토큰이 반환된다`() {
        val response = "valid_token"
        every { memberAuthenticationService.generateTokenByRegister(any()) } returns response
        every { mailService.sendAuthenticationCodeMail(any(), any()) } just Runs

        mockMvc.post("/api/members/register") {
            jsonContent(createRegisterMemberRequest())
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("member-register-post"))
        }
    }

    @Test
    fun `올바른 회원 로그인 요청에 응답으로 Token을 반환한다`() {
        val response = "valid_token"
        every { memberAuthenticationService.generateTokenByLogin(any()) } returns response

        mockMvc.post("/api/members/login") {
            jsonContent(createAuthenticateMemberRequest())
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("member-login-post"))
        }
    }

    @Test
    fun `잘못된 회원 로그인 요청에 응답으로 403 Forbidden을 반환한다`() {
        every { memberAuthenticationService.generateTokenByLogin(any()) } throws UnidentifiedMemberException("사용자 정보가 일치하지 않습니다.")

        mockMvc.post("/api/members/login") {
            jsonContent(createAuthenticateMemberRequest(password = INVALID_PASSWORD))
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            handle(document("member-login-post-forbidden"))
        }
    }

    @Test
    fun `올바른 비밀번호 찾기 요청에 응답으로 NoContent를 반환한다`() {
        every { memberService.resetPassword(any()) } just Runs

        mockMvc.post("/api/members/reset-password") {
            jsonContent(ResetPasswordRequest("회원", "test@email.com", createLocalDate(1995, 2, 2)))
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("member-reset-password-post"))
        }
    }

    @Test
    fun `잘못된 비밀번호 찾기 요청에 응답으로 403 Forbidden을 반환한다`() {
        every { memberService.resetPassword(any()) } throws UnidentifiedMemberException("사용자 정보가 일치하지 않습니다.")

        mockMvc.post("/api/members/reset-password") {
            jsonContent(ResetPasswordRequest("회원", "test@email.com", createLocalDate(1995, 4, 4)))
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            handle(document("member-reset-password-post-forbidden"))
        }
    }

    @Test
    fun `올바른 비밀번호 변경 요청에 응답으로 NoContent를 반환한다`() {
        every { memberService.editPassword(any(), any()) } just Runs

        mockMvc.post("/api/members/edit-password") {
            jsonContent(createEditPasswordRequest())
            bearer("valid_token")
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("member-edit-password-post"))
        }
    }

    @Test
    fun `잘못된 비밀번호 변경 요청에 응답으로 403 Forbidden을 반환한다`() {
        every { memberService.editPassword(any(), any()) } throws UnidentifiedMemberException("기존 비밀번호가 일치하지 않습니다.")

        mockMvc.post("/api/members/edit-password") {
            jsonContent(createEditPasswordRequest(oldPassword = WRONG_PASSWORD))
            bearer("valid_token")
        }.andExpect {
            status { isForbidden() }
        }.andDo {
            handle(document("member-edit-password-post-forbidden"))
        }
    }

    @Test
    fun `이메일 인증 코드 요청에 응답으로 NoContent를 반환한다`() {
        val authenticationCode = AuthenticationCode("authentication-code@email.com")
        every { memberAuthenticationService.generateAuthenticationCode(any()) } returns authenticationCode.code
        every { mailService.sendAuthenticationCodeMail(any(), any()) } just Runs

        mockMvc.post("/api/members/authentication-code") {
            param("email", authenticationCode.email)
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("member-authentication-code-post"))
        }
    }

    @Test
    fun `이메일 인증 요청에 응답으로 NoContent를 반환한다`() {
        every { memberAuthenticationService.authenticateEmail(any(), any()) } just Runs

        mockMvc.post("/api/members/authenticate-email") {
            param("email", "test@email.com")
            param("authenticationCode", "code")
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("member-authenticate-email-post"))
        }
    }

    @Test
    fun `키워드(이름 or 이메일)로 회원들을 조회한다`() {
        val responses = listOf(MemberResponse(createMember("아마찌")))
        every { memberService.findAllByKeyword(any()) } returns responses

        mockMvc.get("/api/members") {
            bearer("valid_token")
            param("keyword", "아마찌")
        }.andExpect {
            status { isOk() }
            content { success(responses) }
        }
    }

    @Test
    fun `회원이 자신의 정보를 조회한다`() {
        val response = MemberResponse(createMember())
        every { memberService.getInformation(any()) } returns response

        mockMvc.get("/api/members/me") {
            bearer("valid_token")
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }.andDo {
            handle(document("member-me-get"))
        }
    }

    @Test
    fun `회원이 정보를 변경한다`() {
        every { memberService.editInformation(any(), any()) } just Runs

        mockMvc.patch("/api/members/information") {
            jsonContent(EditInformationRequest("010-9999-9999"))
            bearer("valid_token")
        }.andExpect {
            status { isNoContent() }
        }.andDo {
            handle(document("member-information-patch"))
        }
    }
}
