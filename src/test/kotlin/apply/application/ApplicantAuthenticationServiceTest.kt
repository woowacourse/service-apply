package apply.application

import apply.BIRTHDAY
import apply.EMAIL
import apply.GENDER
import apply.NAME
import apply.PASSWORD
import apply.PHONE_NUMBER
import apply.VALID_TOKEN
import apply.WRONG_PASSWORD
import apply.createApplicant
import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantAuthenticationException
import apply.domain.applicant.ApplicantRepository
import apply.domain.authenticationcode.AuthenticationCode
import apply.domain.authenticationcode.AuthenticationCodeRepository
import apply.domain.authenticationcode.getLastByEmail
import apply.security.JwtTokenProvider
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import support.test.UnitTest

@UnitTest
internal class ApplicantAuthenticationServiceTest {
    @MockK
    private lateinit var applicantRepository: ApplicantRepository

    @MockK
    private lateinit var authenticationCodeRepository: AuthenticationCodeRepository

    @MockK
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var applicantAuthenticationService: ApplicantAuthenticationService

    @BeforeEach
    internal fun setUp() {
        every { jwtTokenProvider.createToken(any()) } returns VALID_TOKEN
        applicantAuthenticationService = ApplicantAuthenticationService(
            applicantRepository, authenticationCodeRepository, jwtTokenProvider
        )
    }

    @DisplayName("토큰 생성은")
    @Nested
    inner class GenerateToken {
        private lateinit var request: RegisterApplicantRequest

        fun subject(): String {
            return applicantAuthenticationService.generateToken(request)
        }

        @Test
        fun `지원자가 존재하고 인증에 성공하면 유효한 토큰을 반환한다`() {
            every { applicantRepository.findByEmail(any()) } answers { createApplicant() }
            request = RegisterApplicantRequest(NAME, EMAIL, PHONE_NUMBER, GENDER, BIRTHDAY, PASSWORD)
            assertThat(subject()).isEqualTo(VALID_TOKEN)
        }

        @Test
        fun `지원자가 존재하지만 인증에 실패하면 예외가 발생한다`() {
            every { applicantRepository.findByEmail(any()) } answers { createApplicant() }
            request = RegisterApplicantRequest("가짜 이름", EMAIL, PHONE_NUMBER, GENDER, BIRTHDAY, PASSWORD)
            assertThrows<ApplicantAuthenticationException> { subject() }
        }

        @Test
        fun `지원자가 존재하지 않다면 지원자를 저장한 뒤, 유효한 토큰을 반환한다`() {
            every { applicantRepository.findByEmail(any()) } answers { null }
            every { applicantRepository.save(any<Applicant>()) } returns createApplicant()
            request = RegisterApplicantRequest(NAME, EMAIL, PHONE_NUMBER, GENDER, BIRTHDAY, PASSWORD)
            assertThat(subject()).isEqualTo(VALID_TOKEN)
        }
    }

    @DisplayName("(로그인) 토큰 생성은")
    @Nested
    inner class GenerateTokenByLogin {
        private lateinit var request: AuthenticateApplicantRequest

        fun subject(): String {
            return applicantAuthenticationService.generateTokenByLogin(request)
        }

        @Test
        fun `지원자가 존재하고 인증에 성공하면 유효한 토큰을 반환한다`() {
            every { applicantRepository.findByEmail(any()) } answers { createApplicant() }
            request = AuthenticateApplicantRequest(EMAIL, PASSWORD)
            assertThat(subject()).isEqualTo(VALID_TOKEN)
        }

        @Test
        fun `지원자가 존재하지만 인증에 실패하면 예외가 발생한다`() {
            every { applicantRepository.findByEmail(any()) } answers { createApplicant() }
            request = AuthenticateApplicantRequest(EMAIL, WRONG_PASSWORD)
            assertThrows<ApplicantAuthenticationException> { subject() }
        }

        @Test
        fun `지원자가 존재하지 않다면 예외가 발생한다`() {
            every { applicantRepository.findByEmail(any()) } answers { null }
            request = AuthenticateApplicantRequest(EMAIL, PASSWORD)
            assertThrows<ApplicantAuthenticationException> { subject() }
        }
    }

    @DisplayName("이메일 사용자 인증 시")
    @Nested
    inner class AuthenticateEmail {
        private val authenticationCode = AuthenticationCode("test@email.com")

        @Test
        fun `인증 코드가 일치한다면 인증된 사용자로 변경한다`() {
            every { authenticationCodeRepository.getLastByEmail(any()) } returns authenticationCode
            applicantAuthenticationService.authenticateEmail(authenticationCode.email, authenticationCode.code)
            assertThat(authenticationCode.authenticated).isTrue()
        }

        @Test
        fun `인증 코드가 일치하지 않는다면 예외가 발생한다`() {
            every { authenticationCodeRepository.getLastByEmail(any()) } returns authenticationCode
            assertThrows<IllegalArgumentException> {
                applicantAuthenticationService.authenticateEmail(authenticationCode.email, "INVALID")
            }
        }

        @Test
        fun `인증 코드를 생성한다`() {
            every { applicantRepository.existsByEmail(any()) } returns false
            every { authenticationCodeRepository.existsByEmailAndAuthenticatedTrue(any()) } returns false
            every { authenticationCodeRepository.save(any()) } returns authenticationCode
            val actual = applicantAuthenticationService.generateAuthenticationCode(authenticationCode.email)
            assertAll(
                { assertThat(actual).isEqualTo(authenticationCode.code) },
                { assertThat(authenticationCode.authenticated).isFalse() }
            )
        }

        @Test
        fun `인증코드 요청시 이미 가입된 이메일이라면 예외가 발생한다`() {
            every { applicantRepository.existsByEmail(any()) } returns true
            assertThrows<IllegalStateException> {
                applicantAuthenticationService.generateAuthenticationCode(authenticationCode.email)
            }
        }

        @Test
        fun `인증코드 요청시 이미 인증된 이메일이라면 예외가 발생한다`() {
            every { applicantRepository.existsByEmail(any()) } returns false
            every { authenticationCodeRepository.existsByEmailAndAuthenticatedTrue(any()) } returns true
            assertThrows<IllegalStateException> {
                applicantAuthenticationService.generateAuthenticationCode(authenticationCode.email)
            }
        }
    }
}
