package apply.application

import apply.domain.applicant.ApplicantInformation
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantVerifyInformation
import apply.domain.applicant.Gender
import apply.domain.applicant.exception.ApplicantValidateException
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import apply.security.JwtTokenProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anySet
import org.mockito.ArgumentMatchers.refEq
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import support.createLocalDate
import support.createLocalDateTime

private const val VALID_TOKEN = "SOME_VALID_TOKEN"
private const val APPLICANT_ID = 1L

@ExtendWith(MockitoExtension::class)
internal class ApplicantServiceTest {
    @Mock
    private lateinit var applicationFormRepository: ApplicationFormRepository

    @Mock
    private lateinit var applicantRepository: ApplicantRepository

    @Mock
    private lateinit var cheaterService: CheaterService

    @Mock
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var applicantService: ApplicantService

    private val applicationForm = ApplicationForm(
        referenceUrl = "",
        submitted = true,
        createdDateTime = createLocalDateTime(2019, 10, 25, 10),
        modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
        submittedDateTime = createLocalDateTime(2019, 11, 5, 10, 10, 10),
        recruitmentId = 1L,
        applicantId = 1L,
        answers = Answers(
            mutableListOf(
                Answer("고객에게 가치를 전달하고 싶습니다.", 1L),
                Answer("도전, 끈기", 2L)
            )
        )
    )

    private val validApplicantRequest = ApplicantInformation(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = "password"
    )

    private val validApplicantLoginRequest = ApplicantVerifyInformation(
        name = validApplicantRequest.name,
        email = validApplicantRequest.email,
        birthday = validApplicantRequest.birthday,
        password = validApplicantRequest.password
    )

    private val inValidApplicantRequest = validApplicantRequest.copy(password = "invalid_password")

    private val inValidApplicantLoginRequest = validApplicantLoginRequest.copy(password = "invalid_password")

    private val applicants = listOf(validApplicantRequest.toEntity(APPLICANT_ID))

    private val applicantResponses = listOf(ApplicantResponse(applicants[0], true, applicationForm))

    @BeforeEach
    internal fun setUp() {
        applicantService =
            ApplicantService(applicationFormRepository, applicantRepository, cheaterService, jwtTokenProvider)
    }

    @Test
    fun `지원자 정보와 부정 행위자 여부를 함께 제공한다`() {
        given(applicationFormRepository.findByRecruitmentId(anyLong())).willReturn(listOf(applicationForm))
        given(applicantRepository.findAllById(anySet())).willReturn(applicants)
        given(cheaterService.existsByApplicantId(APPLICANT_ID)).willReturn(true)

        val founds = applicantService.findAllByRecruitmentId(1L)

        assertAll(
            { assertThat(founds).usingElementComparatorIgnoringFields("isCheater").isEqualTo(applicantResponses) },
            { assertThat(founds[0].isCheater).isEqualTo(true) }
        )
    }

    @Test
    fun `지원자가 이미 존재하고 검증에 성공하면 유효한 토큰을 반환한다`() {
        val applicant = validApplicantRequest.toEntity(APPLICANT_ID)
        given(applicantRepository.findByEmail(validApplicantRequest.email)).willReturn(applicant)
        given(jwtTokenProvider.createToken(validApplicantRequest.email)).willReturn(VALID_TOKEN)

        assertThat(applicantService.generateToken(validApplicantRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `지원자가 이미 존재하고 필드 값 동등성 검증에 실패하면 예외가 발생한다`() {
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

    @Test
    fun `(로그인) 지원자가 존재할시, 유효한 토큰을 반환한다`() {
        given(
            applicantRepository.existsByNameAndEmailAndBirthdayAndPassword(
                validApplicantLoginRequest.name,
                validApplicantLoginRequest.email,
                validApplicantLoginRequest.birthday,
                validApplicantLoginRequest.password
            )
        ).willReturn(true)
        given(jwtTokenProvider.createToken(validApplicantLoginRequest.email)).willReturn(VALID_TOKEN)

        assertThat(applicantService.generateTokenByLogin(validApplicantLoginRequest)).isEqualTo(VALID_TOKEN)
    }

    @Test
    fun `(로그인) 지원자가 존재하지 않을시, 예외가 발생한다`() {
        given(
            applicantRepository.existsByNameAndEmailAndBirthdayAndPassword(
                inValidApplicantLoginRequest.name,
                inValidApplicantLoginRequest.email,
                inValidApplicantLoginRequest.birthday,
                inValidApplicantLoginRequest.password
            )
        ).willReturn(false)

        assertThatThrownBy { applicantService.generateTokenByLogin(inValidApplicantLoginRequest) }
            .isInstanceOf(ApplicantValidateException::class.java)
            .hasMessage("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
    }
}
