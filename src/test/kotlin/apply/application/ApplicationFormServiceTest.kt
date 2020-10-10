package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
@DataJpaTest
class ApplicationFormServiceTest {
    @MockK
    private lateinit var applicationFormRepository: ApplicationFormRepository

    @MockK
    private lateinit var recruimentRepository: RecruitmentRepository

    @MockK
    private lateinit var applicantService: ApplicantService

    private lateinit var applicationFormService: ApplicationFormService

    private lateinit var applicationForm1: ApplicationForm
    private lateinit var applicationForm2: ApplicationForm
    private lateinit var applicationFormResponse: ApplicationFormResponse
    private lateinit var updateApplicationFormRequest: UpdateApplicationFormRequest
    private lateinit var updateApplicationFormRequestWithPassword: UpdateApplicationFormRequest

    private lateinit var recruitment: Recruitment
    private lateinit var recruitmentNotRecruiting: Recruitment

    @BeforeEach
    internal fun setUp() {
        this.applicationFormService =
            ApplicationFormService(applicationFormRepository, recruimentRepository, applicantService)

        applicationForm1 = ApplicationForm(
            applicantId = 1L,
            recruitmentId = 1L,
            referenceUrl = "http://example.com",
            answers = Answers(
                mutableListOf(
                    Answer("스타트업을 하고 싶습니다.", 1L),
                    Answer("책임감", 2L)
                )
            )
        )

        applicationForm2 = ApplicationForm(
            applicantId = 2L,
            recruitmentId = 1L,
            referenceUrl = "http://example2.com",
            answers = Answers(
                mutableListOf(
                    Answer("대기업에 취직하고 싶습니다.", 1L),
                    Answer("신중함", 2L)
                )
            )
        )

        applicationFormResponse = ApplicationFormResponse(
            id = applicationForm1.id,
            recruitmentId = applicationForm1.recruitmentId,
            referenceUrl = applicationForm1.referenceUrl,
            submitted = applicationForm1.submitted,
            answers = applicationForm1.answers.items.map { AnswerResponse(it.contents, it.recruitmentItemId) },
            createdDateTime = applicationForm1.createdDateTime,
            modifiedDateTime = applicationForm1.modifiedDateTime,
            submittedDateTime = applicationForm1.submittedDateTime
        )

        updateApplicationFormRequest = UpdateApplicationFormRequest(
            recruitmentId = applicationForm1.recruitmentId,
            referenceUrl = applicationForm1.referenceUrl,
            isSubmitted = false,
            answers = applicationForm1.answers.items.map { AnswerRequest(it.contents, it.recruitmentItemId) }
        )

        updateApplicationFormRequestWithPassword = UpdateApplicationFormRequest(
            recruitmentId = applicationForm1.recruitmentId,
            referenceUrl = applicationForm1.referenceUrl,
            isSubmitted = false,
            answers = applicationForm1.answers.items.map { AnswerRequest(it.contents, it.recruitmentItemId) },
            password = "12345678"
        )

        recruitment = Recruitment(
            title = "지원할 제목",
            startDateTime = LocalDateTime.MIN,
            endDateTime = LocalDateTime.MAX,
            canRecruit = true,
            isHidden = false
        )

        recruitmentNotRecruiting = Recruitment(
            title = "지원할 제목",
            startDateTime = LocalDateTime.now().minusHours(2),
            endDateTime = LocalDateTime.now().minusHours(1),
            canRecruit = true,
            isHidden = false
        )
    }

    @Test
    fun `특정 지원의 지원서를 모두 불러온다`() {
        every { applicationFormRepository.findByRecruitmentId(any()) } returns arrayListOf(
            applicationForm1,
            applicationForm2
        )

        assertThat(applicationFormService.findAllByRecruitmentId(1L)).hasSize(2)
    }

    @Test
    fun `관리자가 특정 지원자의 지원서를 불러온다`() {
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationForm1

        assertThat(applicationFormService.getByRecruitmentIdAndApplicantId(1L, 1L)).isEqualTo(applicationForm1)
    }

    @Test
    fun `지원자가 자신의 지원서를 불러온다`() {
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationForm1

        assertThat(applicationFormService.findForm(1L, 1L)).isEqualTo(applicationFormResponse)
    }

    @Test
    fun `지원서가 없으면 오류를 리턴한다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        assertThatIllegalArgumentException().isThrownBy { applicationFormService.findForm(1L, 1L) }
            .withMessage("해당하는 지원서가 없습니다.")
    }

    @Test
    fun `지원서를 생성한다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns false
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()

        assertDoesNotThrow { applicationFormService.create(1L, CreateApplicationFormRequest(1L)) }
    }

    @Test
    fun `지원서를 저장한다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()

        assertDoesNotThrow { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `지원서가 없는 경우 저장할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        assertThatIllegalArgumentException().isThrownBy { applicationFormService.update(1L, updateApplicationFormRequest) }
            .withMessage("작성중인 지원서가 존재하지 않습니다.")
    }

    @Test
    fun `비밀번호가 있는 경우 비밀번호를 업데이트 한다`() {
        every { applicantService.changePassword(any(), any()) } returns mockk()
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationForm1

        applicationFormService.update(1L, updateApplicationFormRequestWithPassword)
        assertDoesNotThrow { applicationFormRepository.save(applicationForm1) }
        verify { applicantService.changePassword(1L, updateApplicationFormRequestWithPassword.password) }
    }

    @Test
    fun `모집이 존재하지 않는 경우 지원을 할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns null

        assertThrows<IllegalArgumentException> { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `모집중이 아닌 지원서에 대해 저장 및 수정할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitmentNotRecruiting

        assertThrows<IllegalStateException> { applicationFormService.update(1L, updateApplicationFormRequest) }
    }
}
