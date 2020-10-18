package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import support.createLocalDate
import support.createLocalDateTime

private const val APPLICANT_ID = 1L
private const val RANDOM_PASSWORD = "nEw_p@ssw0rd"

@ExtendWith(MockKExtension::class)
internal class ApplicantServiceTest {
    @MockK
    private lateinit var applicationFormRepository: ApplicationFormRepository

    @MockK
    private lateinit var applicantRepository: ApplicantRepository

    @MockK
    private lateinit var cheaterRepository: CheaterRepository

    @MockK
    private lateinit var passwordGenerator: PasswordGenerator

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

    private val validApplicantRequest = RegisterApplicantRequest(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = Password("password")
    )

    private val validApplicantPasswordFindRequest = ResetPasswordRequest(
        name = validApplicantRequest.name,
        email = validApplicantRequest.email,
        birthday = validApplicantRequest.birthday
    )

    private val inValidApplicantPasswordFindRequest =
        validApplicantPasswordFindRequest.copy(birthday = createLocalDate(1995, 4, 4))

    private val validEditPasswordRequest = EditPasswordRequest(
        password = Password("password"),
        newPassword = Password("NEW_PASSWORD")
    )

    private val inValidEditPasswordRequest = validEditPasswordRequest.copy(password = Password("wrongPassword"))

    private val applicant: Applicant =
        Applicant(validApplicantRequest.information, validApplicantRequest.password, APPLICANT_ID)

    private val applicantResponses = listOf(ApplicantResponse(applicant, true, applicationForm))

    @BeforeEach
    internal fun setUp() {
        applicantService =
            ApplicantService(applicationFormRepository, applicantRepository, cheaterRepository, passwordGenerator)
    }

    @Test
    fun `지원자 정보와 부정 행위자 여부를 함께 제공한다`() {
        every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(any()) } returns listOf(applicationForm)
        every { applicantRepository.findAllById(any()) } returns listOf(applicant)
        every { cheaterRepository.findAll() } returns listOf(Cheater(1L))

        val founds = applicantService.findAllByRecruitmentIdAndSubmittedTrue(1L)

        assertAll(
            { assertThat(founds).usingElementComparatorIgnoringFields("isCheater").isEqualTo(applicantResponses) },
            { assertThat(founds[0].isCheater).isEqualTo(true) }
        )
    }

    @Test
    fun `지원자의 비밀번호를 초기화한다`() {
        every { applicantRepository.findByEmail(validApplicantPasswordFindRequest.email) } returns applicant
        every { passwordGenerator.generate() } returns RANDOM_PASSWORD
        assertThat(applicantService.resetPassword(validApplicantPasswordFindRequest)).isEqualTo(RANDOM_PASSWORD)
    }

    @Test
    fun `비밀번호를 초기화를 위한 검증 데이터가 올바르지 않을시 예외가 발생한다`() {
        every { applicantRepository.findByEmail(inValidApplicantPasswordFindRequest.email) } returns applicant
        every { passwordGenerator.generate() } returns RANDOM_PASSWORD
        assertThatThrownBy { applicantService.resetPassword(inValidApplicantPasswordFindRequest) }
            .isInstanceOf(ApplicantValidateException::class.java)
    }

    @Test
    fun `지원자의 비밀번호를 수정한다`() {
        every { applicantRepository.getOne(applicant.id) } returns applicant
        assertDoesNotThrow { applicantService.editPassword(applicant, validEditPasswordRequest) }
    }

    @Test
    fun `비밀번호 수정시 기존 비밀번호를 다르게 입력했을경우 예외가 발생한다`() {
        every { applicantRepository.getOne(applicant.id) } returns applicant
        assertThatThrownBy { applicantService.editPassword(applicant, inValidEditPasswordRequest) }
            .isInstanceOf(ApplicantValidateException::class.java)
            .hasMessage("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
    }
}
