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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
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
    private lateinit var applicationFormSubmitted: ApplicationForm
    private lateinit var applicationFormResponse: ApplicationFormResponse
    private lateinit var createApplicationFormRequest: CreateApplicationFormRequest
    private lateinit var updateApplicationFormRequest: UpdateApplicationFormRequest
    private lateinit var updateApplicationFormRequestWithPassword: UpdateApplicationFormRequest

    private lateinit var recruitment: Recruitment
    private lateinit var recruitmentNotRecruiting: Recruitment
    private lateinit var recruitmentHidden: Recruitment

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

        applicationFormSubmitted = ApplicationForm(
            applicantId = 3L,
            recruitmentId = 1L,
            referenceUrl = "http://example2.com",
            answers = Answers(
                mutableListOf(
                    Answer("대기업에 취직하고 싶습니다.", 1L),
                    Answer("신중함", 2L)
                )
            )
        ).apply { submit() }

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

        createApplicationFormRequest = CreateApplicationFormRequest(1L)

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

        recruitmentHidden = Recruitment(
            title = "지원할 제목",
            startDateTime = LocalDateTime.MIN,
            endDateTime = LocalDateTime.MAX,
            canRecruit = true,
            isHidden = true
        )
    }

    @Test
    fun `특정 모집의 지원서를 모두 불러온다`() {
        every { applicationFormRepository.findByRecruitmentId(any()) } returns arrayListOf(
            applicationForm1,
            applicationForm2
        )

        assertThat(applicationFormService.findAllByRecruitmentId(1L)).hasSize(2)
    }

    @Test
    fun `지원자 아이디와 모집 아이디로 지원서를 불러온다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        assertThat(applicationFormService.getByRecruitmentIdAndApplicantId(1L, 1L)).isEqualTo(applicationForm1)
    }

    @Test
    fun `지원서가 있으면 지원서를 불러온다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        assertThat(applicationFormService.findForm(1L, 1L)).isEqualTo(applicationFormResponse)
    }

    @Test
    fun `지원서가 없으면 예외를 던진다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        assertThrows<IllegalArgumentException> { applicationFormService.findForm(1L, 1L) }
    }

    @Test
    fun `지원서를 생성한다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns false
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()

        assertDoesNotThrow { applicationFormService.create(1L, createApplicationFormRequest) }
    }

    @Test
    fun `지원서가 있는 경우 지원할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns true

        assertThrows<IllegalArgumentException> { applicationFormService.create(1L, createApplicationFormRequest) }
    }

    @Test
    fun `모집이 없는 경우 지원할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns null

        assertThrows<IllegalArgumentException> { applicationFormService.create(1L, createApplicationFormRequest) }
    }

    @Test
    fun `지원서를 수정한다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()

        assertDoesNotThrow { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `비밀번호가 있는 경우 비밀번호를 수정한다`() {
        every { applicantService.changePassword(any(), any()) } returns mockk()
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        applicationFormService.update(1L, updateApplicationFormRequestWithPassword)

        verify { applicantService.changePassword(1L, updateApplicationFormRequestWithPassword.password) }
    }

    @Test
    fun `지원서가 없는 경우 수정할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        assertThrows<IllegalArgumentException> { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `모집이 없는 경우 지원서를 수정할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns null

        assertThrows<IllegalArgumentException> { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `모집중이 아닌 지원서를 수정할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitmentNotRecruiting

        assertThrows<IllegalStateException> { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `제출한 지원서를 수정할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationFormSubmitted

        assertThrows<IllegalStateException> { applicationFormService.update(3L, updateApplicationFormRequest) }
    }
}
