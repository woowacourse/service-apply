package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.security.JwtTokenProvider
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import support.createLocalDate

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val APPLICANT_ID = 1L

@ExtendWith(MockKExtension::class)
internal class ApplicantVerificationServiceTest {
    @MockK
    private lateinit var applicantRepository: ApplicantRepository

    @MockK
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var applicantVerificationService: ApplicantVerificationService

    @BeforeEach
    internal fun setUp() {
        applicantVerificationService = ApplicantVerificationService(applicantRepository, jwtTokenProvider)
    }

    private val validApplicantRequest = RegisterApplicantRequest(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = Password("password")
    )

    private val validApplicantLoginRequest = VerifyApplicantRequest(
        email = validApplicantRequest.email,
        password = validApplicantRequest.password
    )

    private val inValidApplicantRequest = validApplicantRequest.copy(password = Password("invalid_password"))

    private val inValidApplicantLoginRequest = validApplicantLoginRequest.copy(password = Password("invalid_password"))

    private val applicant = Applicant(validApplicantRequest.information, validApplicantRequest.password, APPLICANT_ID)

    @Test
    fun `지원자가 이미 존재하고 검증에 성공하면 유효한 토큰을 반환한다`() {
        every { applicantRepository.findByEmail("test@email.com") } returns applicant
        every { jwtTokenProvider.createToken("test@email.com") } returns VALID_TOKEN
        assertThat(applicantVerificationService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `지원자가 이미 존재하고 필드 값 동등성 검증에 실패하면 예외가 발생한다`() {
        every { applicantRepository.findByEmail(inValidApplicantRequest.email) } returns applicant
        assertThatThrownBy {
            applicantVerificationService.generateToken(inValidApplicantRequest)
        }.isInstanceOf(ApplicantValidateException::class.java)
    }

    @Test
    fun `지원자가 존재하지 않으면 지원자를 저장한 뒤, 유효한 토큰을 반환한다`() {
        every { applicantRepository.findByEmail(validApplicantRequest.email) } returns null
        every { jwtTokenProvider.createToken(validApplicantRequest.email) } returns VALID_TOKEN
        every { applicantRepository.save(nrefEq(validApplicantRequest.toEntity())) } returns applicant
        assertThat(applicantVerificationService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `(로그인) 지원자가 존재할시, 유효한 토큰을 반환한다`() {
        every { applicantRepository.findByEmail(validApplicantLoginRequest.email) } returns applicant
        every { jwtTokenProvider.createToken(validApplicantRequest.email) } returns VALID_TOKEN
        assertThat(applicantVerificationService.generateTokenByLogin(validApplicantLoginRequest))
            .isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `(로그인) 지원자가 존재하지 않을시, 예외가 발생한다`() {
        every { applicantRepository.findByEmail(inValidApplicantLoginRequest.email) } returns applicant
        assertThatThrownBy { applicantVerificationService.generateTokenByLogin(inValidApplicantLoginRequest) }
            .isInstanceOf(ApplicantValidateException::class.java)
    }
}
