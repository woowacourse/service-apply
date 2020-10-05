package apply.security

import apply.application.ApplicantService
import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import support.createLocalDate

@ExtendWith(MockKExtension::class)
internal class LoginApplicantResolverTest {
    @MockK
    private lateinit var applicantService: ApplicantService

    @MockK
    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var loginApplicantResolver: LoginApplicantResolver
    private lateinit var methodParameter: MethodParameter
    private lateinit var nativeWebRequest: NativeWebRequest

    @BeforeEach
    internal fun setUp() {
        loginApplicantResolver = LoginApplicantResolver(jwtTokenProvider, applicantService)
        methodParameter = mockk()
        nativeWebRequest = mockk()
    }

    @ParameterizedTest
    @CsvSource("authTargetMethod,true", "nonAuthTargetMethod,false")
    fun `@LoginApplicant 주어진 메서드의 서포트 여부를 확인한다`(methodName: String, expected: Boolean) {
        val method = TestAuthController::class.java.getDeclaredMethod(methodName, Applicant::class.java)
        val loginApplicantParameter: MethodParameter = MethodParameter.forExecutable(method, 0)

        assertThat(loginApplicantResolver.supportsParameter(loginApplicantParameter)).isEqualTo(expected)
    }

    private class TestAuthController {
        fun authTargetMethod(@LoginApplicant applicant: Applicant) {}

        fun nonAuthTargetMethod(applicant: Applicant) {}
    }

    @Test
    fun `요청의 Authorization 헤더로 저장된 지원자를 불러온다`() {
        every { nativeWebRequest.getHeader(AUTHORIZATION_HEADER) } returns "Bearer valid_token"
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns "applicant_email@email.com"
        val expectedApplicant = Applicant(
            name = "홍길동1",
            email = "applicant_email@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 4, 17),
            password = "password"
        )
        every { applicantService.getByEmail("applicant_email@email.com") } returns expectedApplicant

        val result = loginApplicantResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedApplicant)
    }

    @ParameterizedTest
    @CsvSource(
        "Bearertokeninfo",
        "''",
        "Bearer"
    )
    fun `요청의 Authorization 헤더의 형식이 올바르지 않을 경우 예외가 발생한다`(header: String) {
        every { nativeWebRequest.getHeader(AUTHORIZATION_HEADER) } returns header

        assertThatIllegalArgumentException().isThrownBy {
            loginApplicantResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }.withMessage("로그인 정보가 정확하지 않습니다")
    }

    @Test
    fun `요청의 Authorization 헤더가 존재하지 않을 경우 예외가 발생한다`() {
        every { nativeWebRequest.getHeader(AUTHORIZATION_HEADER) } returns null

        assertThatIllegalArgumentException().isThrownBy {
            loginApplicantResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }.withMessage("로그인 정보가 정확하지 않습니다")
    }

    @Test
    fun `요청의 토큰이 유효하지 않은 경우 예외가 발생한다`() {
        every { nativeWebRequest.getHeader(AUTHORIZATION_HEADER) } returns "invalid_token"
        every { jwtTokenProvider.isValidToken("invalid_token") } returns false

        assertThatIllegalArgumentException().isThrownBy {
            loginApplicantResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }.withMessage("로그인 정보가 정확하지 않습니다")
    }
}
