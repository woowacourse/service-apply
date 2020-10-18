package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.security.JwtTokenProvider
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import support.createLocalDate

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val APPLICANT_ID = 1L

@ExtendWith(MockitoExtension::class)
internal class ApplicantVerificationServiceTest {
    @Mock
    private lateinit var applicantRepository: ApplicantRepository

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var applicantVerificationService: ApplicantVerificationService

    private val validApplicantRequest = ApplicantInformation(
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

    private val applicant = validApplicantRequest.toEntity(APPLICANT_ID)

    @BeforeEach
    internal fun setUp() {
        applicantVerificationService = ApplicantVerificationService(applicantRepository, jwtTokenProvider)
    }

    @Test
    fun `지원자가 이미 존재하고 검증에 성공하면 유효한 토큰을 반환한다`() {
        BDDMockito.given(applicantRepository.findByEmail(validApplicantRequest.email)).willReturn(applicant)
        BDDMockito.given(jwtTokenProvider.createToken(validApplicantRequest.email)).willReturn(VALID_TOKEN)

        Assertions.assertThat(applicantVerificationService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `지원자가 이미 존재하고 필드 값 동등성 검증에 실패하면 예외가 발생한다`() {
        BDDMockito.given(applicantRepository.findByEmail(inValidApplicantRequest.email)).willReturn(applicant)

        Assertions.assertThatThrownBy {
            applicantVerificationService.generateToken(inValidApplicantRequest)
        }.isInstanceOf(ApplicantValidateException::class.java)
            .hasMessage("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
    }

    @Test
    fun `지원자가 존재하지 않으면 지원자를 저장한 뒤, 유효한 토큰을 반환한다`() {
        BDDMockito.given(applicantRepository.findByEmail(validApplicantRequest.email)).willReturn(null)
        BDDMockito.given(jwtTokenProvider.createToken(validApplicantRequest.email)).willReturn(VALID_TOKEN)
        BDDMockito.given(
            applicantRepository.save(
                ArgumentMatchers.refEq(
                    validApplicantRequest.toEntity()
                )
            )
        ).willReturn(applicant)

        Assertions.assertThat(applicantVerificationService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `(로그인) 지원자가 존재할시, 유효한 토큰을 반환한다`() {
        BDDMockito.given(
            applicantRepository.existsByEmailAndPassword(
                validApplicantLoginRequest.email,
                validApplicantLoginRequest.password
            )
        ).willReturn(true)
        BDDMockito.given(jwtTokenProvider.createToken(validApplicantLoginRequest.email)).willReturn(VALID_TOKEN)

        Assertions.assertThat(applicantVerificationService.generateTokenByLogin(validApplicantLoginRequest))
            .isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `(로그인) 지원자가 존재하지 않을시, 예외가 발생한다`() {
        BDDMockito.given(
            applicantRepository.existsByEmailAndPassword(
                inValidApplicantLoginRequest.email,
                inValidApplicantLoginRequest.password
            )
        ).willReturn(false)

        Assertions.assertThatThrownBy { applicantVerificationService.generateTokenByLogin(inValidApplicantLoginRequest) }
            .isInstanceOf(ApplicantValidateException::class.java)
            .hasMessage("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
    }
}
