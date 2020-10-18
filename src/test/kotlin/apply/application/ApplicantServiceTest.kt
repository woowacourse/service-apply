package apply.application

import apply.BIRTHDAY
import apply.EMAIL
import apply.NAME
import apply.RANDOM_PASSWORD_TEXT
import apply.createApplicant
import apply.createApplicationForm
import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
import apply.domain.applicant.Gender
import apply.domain.applicant.Password
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import support.createLocalDate

private const val APPLICANT_ID = 1L

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

    private val validApplicantRequest = RegisterApplicantRequest(
        name = "지원자",
        email = "test@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(1995, 2, 2),
        password = Password("password")
    )

    private val validEditPasswordRequest = EditPasswordRequest(
        password = Password("password"),
        newPassword = Password("NEW_PASSWORD")
    )

    private val inValidEditPasswordRequest = validEditPasswordRequest.copy(password = Password("wrongPassword"))

    private val applicant: Applicant =
        Applicant(validApplicantRequest.information, validApplicantRequest.password, APPLICANT_ID)

    @BeforeEach
    internal fun setUp() {
        applicantService =
            ApplicantService(applicationFormRepository, applicantRepository, cheaterRepository, passwordGenerator)
    }

    @Test
    fun `지원자 정보와 부정 행위자 여부를 함께 제공한다`() {
        slot<Long>().also { slot ->
            every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(capture(slot)) } answers {
                listOf(createApplicationForm(recruitmentId = slot.captured))
            }
        }
        every { cheaterRepository.findAll() } returns listOf(Cheater(1L))
        slot<Iterable<Long>>().also { slot ->
            every { applicantRepository.findAllById(capture(slot)) } answers {
                slot.captured.map { createApplicant(id = it) }
            }
        }

        val actual = applicantService.findAllByRecruitmentIdAndSubmittedTrue(1L)

        assertThat(actual).hasSize(1)
        assertThat(actual[0].isCheater).isTrue()
    }

    @DisplayName("지원자의 비밀번호는")
    @Nested
    inner class ResetPassword {
        private lateinit var request: ResetPasswordRequest

        @BeforeEach
        internal fun setUp() {
            every { applicantRepository.findByEmail(EMAIL) } returns createApplicant()
            every { passwordGenerator.generate() } returns RANDOM_PASSWORD_TEXT
        }

        fun subject(): String {
            return applicantService.resetPassword(request)
        }

        @Test
        fun `만약 개인정보가 일치한다면 초기화한다`() {
            request = ResetPasswordRequest(NAME, EMAIL, BIRTHDAY)
            assertThat(subject()).isEqualTo(RANDOM_PASSWORD_TEXT)
        }

        @Test
        fun `만약 개인정보가 일치하지 않는다면 예외가 발생한다`() {
            request = ResetPasswordRequest(NAME, EMAIL, createLocalDate(1995, 4, 4))
            assertThatThrownBy { subject() }.isInstanceOf(ApplicantValidateException::class.java)
        }
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
