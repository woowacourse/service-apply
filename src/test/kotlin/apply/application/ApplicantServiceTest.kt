package apply.application

import apply.BIRTHDAY
import apply.EMAIL
import apply.NAME
import apply.PASSWORD
import apply.RANDOM_PASSWORD_TEXT
import apply.createApplicant
import apply.createApplicationForm
import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.ApplicantValidateException
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

    @BeforeEach
    internal fun setUp() {
        applicantService = ApplicantService(
            applicationFormRepository,
            applicantRepository,
            cheaterRepository,
            passwordGenerator
        )
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

    @DisplayName("비밀번호 초기화는")
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

    @DisplayName("비밀번호 변경은")
    @Nested
    inner class EditPassword {
        private lateinit var request: EditPasswordRequest

        @BeforeEach
        internal fun setUp() {
            slot<Long>().also { slot ->
                every { applicantRepository.getOne(capture(slot)) } answers { createApplicant(id = slot.captured) }
            }
        }

        fun subject() {
            applicantService.editPassword(createApplicant(), request)
        }

        @Test
        fun `만약 기존 비밀번호가 일치한다면 변경한다`() {
            request = EditPasswordRequest(PASSWORD, Password("new_password"))
            assertDoesNotThrow { subject() }
        }

        @Test
        fun `만약 기존 비밀번호가 일치하지 않다면 예외가 발생한다`() {
            request = EditPasswordRequest(Password("wrong_password"), Password("new_password"))
            assertThatThrownBy { subject() }.isInstanceOf(ApplicantValidateException::class.java)
        }
    }
}
