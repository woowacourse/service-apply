package apply.application

import apply.createAnswerRequest
import apply.createApplicationForm
import apply.createApplicationForms
import apply.createExceededAnswerRequest
import apply.createRecruitment
import apply.createRecruitmentItem
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.applicationform.ApplicationValidator
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItem
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.pass
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import support.test.UnitTest
import java.time.LocalDateTime

@UnitTest
class ApplicationFormServiceTest {
    @MockK
    private lateinit var applicationFormRepository: ApplicationFormRepository

    @MockK
    private lateinit var recruitmentRepository: RecruitmentRepository

    @MockK
    private lateinit var recruitmentItemRepository: RecruitmentItemRepository

    @MockK
    private lateinit var applicationValidator: ApplicationValidator

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

    private val userId: Long = 1L

    @BeforeEach
    internal fun setUp() {
        this.applicationFormService = ApplicationFormService(
            applicationFormRepository,
            recruitmentRepository,
            recruitmentItemRepository,
            applicationValidator
        )

        applicationForm1 = createApplicationForm()

        applicationForm2 = createApplicationForm(userId = 2L)

        applicationFormSubmitted = createApplicationForm(userId = 3L).apply { submit(pass) }

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

        createApplicationFormRequest = CreateApplicationFormRequest(userId)

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
    fun `지원서가 있으면 지원서를 불러온다`() {
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns applicationForm1

        assertThat(applicationFormService.getApplicationForm(userId, 1L)).isEqualTo(applicationFormResponse)
    }

    @Test
    fun `지원서가 없으면 예외를 던진다`() {
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns null

        val message =
            assertThrows<IllegalArgumentException> { applicationFormService.getApplicationForm(1L, 1L) }.message
        assertThat(message).isEqualTo("해당하는 지원서가 없습니다.")
    }

    @Test
    fun `지원자가 자신의 지원서를 모두 불러온다`() {
        every { applicationFormRepository.findAllByUserId(userId) } returns applicationForms

        val expected = applicationFormService.getMyApplicationForms(userId)

        assertAll(
            { assertThat(expected).isNotNull },
            { assertThat(expected).hasSize(2) }
        )
    }

    @Test
    fun `지원자가 지원한 지원서가 없으면 빈 리스트를 불러온다`() {
        every { applicationFormRepository.findAllByUserId(userId) } returns emptyList()

        val expected = applicationFormService.getMyApplicationForms(userId)

        assertAll(
            { assertThat(expected).isNotNull },
            { assertThat(expected).hasSize(0) }
        )
    }

    @Test
    fun `지원서를 생성한다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndUserId(any(), any()) } returns false
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()
        every { applicationValidator.validate(any(), any()) } just Runs

        assertDoesNotThrow { applicationFormService.create(userId, createApplicationFormRequest) }
    }

    @Test
    fun `이미 작성한 지원서가 있는 경우 지원할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndUserId(any(), any()) } returns true
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()
        every { applicationValidator.validate(any(), any()) } just Runs

        val message = assertThrows<IllegalStateException> {
            applicationFormService.create(
                userId,
                createApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("이미 작성한 지원서가 있습니다.")
    }

    @Test
    fun `모집이 없는 경우 지원할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns null

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.create(
                userId,
                createApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("지원하는 모집이 존재하지 않습니다.")
    }

    @Test
    fun `지원서를 수정한다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns applicationForm1
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems

        assertDoesNotThrow { applicationFormService.update(userId, updateApplicationFormRequest) }
    }

    @Test
    fun `지원서가 없는 경우 수정할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByUserIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns null

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                userId,
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
                userId,
                updateApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("지원 불가능한 모집입니다.")
    }

    @Test
    fun `제출한 지원서를 열람할 수 없다`() {
        every {
            applicationFormRepository.findByRecruitmentIdAndUserId(
                any(),
                any()
            )
        } returns applicationFormSubmitted

        val message = assertThrows<IllegalStateException> {
            applicationFormService.getApplicationForm(
                userId = 3L,
                recruitmentId = 1L
            )
        }.message
        assertThat(message).isEqualTo("이미 제출한 지원서는 열람할 수 없습니다.")
    }

    @Test
    fun `작성하지 않은 문항이 존재하는 경우 제출할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByUserIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns applicationForm1

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                userId,
                UpdateApplicationFormRequest(recruitmentId = 1L, submitted = true)
            )
        }.message
        assertThat(message).isEqualTo("작성하지 않은 문항이 존재합니다.")
    }

    @Test
    fun `유효하지 않은 문항이 존재하는 경우 제출할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByUserIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndUserId(any(), any()) } returns applicationForm1

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                userId,
                UpdateApplicationFormRequest(
                    recruitmentId = 1L,
                    submitted = true,
                    answers = listOf(
                        createExceededAnswerRequest(recruitmentItemId = 1L),
                        createAnswerRequest(recruitmentItemId = 2L)
                    )
                )
            )
        }.message
        assertThat(message).isEqualTo("모집 문항의 최대 글자 수를 초과하였습니다.")
    }
}
