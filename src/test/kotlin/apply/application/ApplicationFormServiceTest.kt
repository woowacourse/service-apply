package apply.application

import apply.createAnswerRequest
import apply.createApplicationForm
import apply.createApplicationForms
import apply.createExcessOfLengthAnswerRequest
import apply.createRecruitment
import apply.createRecruitmentItem
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
    private lateinit var recruitmentRepository: RecruitmentRepository

    @MockK
    private lateinit var recruitmentItemRepository: RecruitmentItemRepository

    @MockK
    private lateinit var applicantService: ApplicantService

    private lateinit var applicationFormService: ApplicationFormService

    private lateinit var applicationForm1: ApplicationForm
    private lateinit var applicationForm2: ApplicationForm
    private lateinit var applicationFormSubmitted: ApplicationForm
    private lateinit var applicationForms: List<ApplicationForm>
    private lateinit var applicationFormResponse: ApplicationFormResponse
    private lateinit var createApplicationFormRequest: CreateApplicationFormRequest
    private lateinit var updateApplicationFormRequest: UpdateApplicationFormRequest
    private lateinit var updateApplicationFormRequestWithPassword: UpdateApplicationFormRequest

    private lateinit var recruitment: Recruitment
    private lateinit var recruitmentItems: List<RecruitmentItem>
    private lateinit var recruitmentNotRecruiting: Recruitment
    private lateinit var recruitmentHidden: Recruitment

    @BeforeEach
    internal fun setUp() {
        this.applicationFormService =
            ApplicationFormService(applicationFormRepository, recruitmentRepository, recruitmentItemRepository)

        applicationForm1 = createApplicationForm()

        applicationForm2 = createApplicationForm(applicantId = 2L)

        applicationFormSubmitted = createApplicationForm(applicantId = 3L).apply { submit() }

        applicationForms = createApplicationForms()

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
            submitted = false,
            answers = applicationForm1.answers.items.map { AnswerRequest(it.contents, it.recruitmentItemId) }
        )

        recruitmentItems = listOf(
            createRecruitmentItem(
                recruitmentId = applicationForm1.recruitmentId,
                position = 1,
                id = 1L
            ),
            createRecruitmentItem(
                recruitmentId = applicationForm1.recruitmentId,
                position = 2,
                id = 2L
            )
        )

        updateApplicationFormRequestWithPassword = UpdateApplicationFormRequest(
            recruitmentId = applicationForm1.recruitmentId,
            referenceUrl = applicationForm1.referenceUrl,
            submitted = false,
            answers = applicationForm1.answers.items.map { AnswerRequest(it.contents, it.recruitmentItemId) }
        )

        recruitment = createRecruitment(hidden = false)

        recruitmentNotRecruiting = createRecruitment(
            startDateTime = LocalDateTime.now().minusHours(2),
            endDateTime = LocalDateTime.now().minusHours(1),
            hidden = false
        )

        recruitmentHidden = createRecruitment()
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

        val message = assertThrows<IllegalArgumentException> { applicationFormService.findForm(1L, 1L) }.message
        assertThat(message).isEqualTo("해당하는 지원서가 없습니다.")
    }

    @Test
    fun `지원자가 자신의 지원서를 모두 불러온다`() {
        every { applicationFormRepository.findAllByApplicantId(1L) } returns applicationForms

        val expected = applicationFormService.getAllByApplicantId(1L)

        assertAll(
            { assertThat(expected).isNotNull },
            { assertThat(expected).hasSize(2) }
        )
    }

    @Test
    fun `지원자가 지원한 지원서가 없으면 빈 리스트를 불러온다`() {
        every { applicationFormRepository.findAllByApplicantId(1L) } returns emptyList()

        val expected = applicationFormService.getAllByApplicantId(1L)

        assertAll(
            { assertThat(expected).isNotNull },
            { assertThat(expected).hasSize(0) }
        )
    }

    @Test
    fun `지원서를 생성한다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns false
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()

        assertDoesNotThrow { applicationFormService.create(1L, createApplicationFormRequest) }
    }

    @Test
    fun `지원서가 있는 경우 지원할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns true

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.create(
                1L,
                createApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("이미 지원한 이력이 있습니다.")
    }

    @Test
    fun `모집이 없는 경우 지원할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns null

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.create(
                1L,
                createApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("지원하는 모집이 존재하지 않습니다.")
    }

    @Test
    fun `지원서를 수정한다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems

        assertDoesNotThrow { applicationFormService.update(1L, updateApplicationFormRequest) }
    }

    @Test
    fun `지원서가 없는 경우 수정할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByApplicantIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                1L,
                updateApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("해당하는 지원서가 없습니다.")
    }

    @Test
    fun `모집중이 아닌 지원서를 수정할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitmentNotRecruiting

        val message = assertThrows<IllegalStateException> {
            applicationFormService.update(
                1L,
                updateApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("지원 불가능한 모집입니다.")
    }

    @Test
    fun `제출한 지원서를 수정할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationFormSubmitted
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems

        val message = assertThrows<IllegalStateException> {
            applicationFormService.update(
                3L,
                updateApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("이미 제출된 지원서입니다. 수정할 수 없습니다.")
    }

    @Test
    fun `단 하나의 지원서만 제출할 수 있다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByApplicantIdAndSubmittedTrue(any()) } returns true
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                1L,
                UpdateApplicationFormRequest(recruitmentId = 1L, submitted = true)
            )
        }.message
        assertThat(message).isEqualTo("이미 제출 완료한 지원서가 존재하여 제출할 수 없습니다.")
    }

    @Test
    fun `작성하지 않은 문항이 존재하는 경우 제출할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByApplicantIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                1L,
                UpdateApplicationFormRequest(recruitmentId = 1L, submitted = true)
            )
        }.message
        assertThat(message).isEqualTo("작성하지 않은 문항이 존재합니다.")
    }

    @Test
    fun `유효하지 않은 문항이 존재하는 경우 제출할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByApplicantIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                1L,
                UpdateApplicationFormRequest(
                    recruitmentId = 1L,
                    submitted = true,
                    answers = listOf(
                        createExcessOfLengthAnswerRequest(recruitmentItemId = 1L),
                        createAnswerRequest(recruitmentItemId = 2L)
                    )
                )
            )
        }.message
        assertThat(message).isEqualTo("유효하지 않은 문항이 존재합니다.")
    }
}
