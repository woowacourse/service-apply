package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.Gender
import apply.domain.applicant.exception.ApplicantValidateException
import apply.security.JwtTokenProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.refEq
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import support.createLocalDate

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val APPLICANT_ID = 1L

@ExtendWith(MockitoExtension::class)
internal class ApplicantServiceTest {
    @Mock
    private lateinit var applicantRepository: ApplicantRepository

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var applicantService: ApplicantService

    private val validApplicantRequest = ApplicantInfo(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = "password"
    )

    private val inValidApplicantRequest = validApplicantRequest.copy(password = "invalid_password")

    @BeforeEach
    internal fun setUp() {
        applicantService = ApplicantService(applicantRepository, jwtTokenProvider)
    }

    @Test
    fun `지원자가 이미 존재하고 검증에 성공하면 유효한 토큰을 반환한다`() {
        val applicant = validApplicantRequest.toEntity(APPLICANT_ID)
        given(applicantRepository.findByEmail(validApplicantRequest.email)).willReturn(applicant)
        given(jwtTokenProvider.createToken(validApplicantRequest.email)).willReturn(VALID_TOKEN)

        assertThat(applicantService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `지원자가 이미 존재하고 필드 값 동등성 검증에 실패하면 ApplicantValidateException이 발생한다`() {
        val applicant = validApplicantRequest.toEntity(APPLICANT_ID)
        given(applicantRepository.findByEmail(inValidApplicantRequest.email)).willReturn(applicant)

        assertThatThrownBy {
            applicantService.generateToken(inValidApplicantRequest)
        }.isInstanceOf(ApplicantValidateException::class.java)
            .hasMessage("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
    }

    @Test
    fun `지원자가 존재하지 않으면 지원자를 저장한 뒤, 유효한 토큰을 반환한다`() {
        given(applicantRepository.findByEmail(validApplicantRequest.email)).willReturn(null)
        given(jwtTokenProvider.createToken(validApplicantRequest.email)).willReturn(VALID_TOKEN)
        given(applicantRepository.save(refEq(validApplicantRequest.toEntity())))
            .willReturn(validApplicantRequest.toEntity(APPLICANT_ID))

        assertThat(applicantService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }
}
