package apply.application

import apply.createAnswerRequest
import apply.createApplicationForm
import apply.createApplicationForms
import apply.createExceededAnswerRequest
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

    private val applicantId: Long = 1L

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

        createApplicationFormRequest = CreateApplicationFormRequest(applicantId)

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
    fun `지원 내역이 없는 경우, 지원 가능하다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.findAllByApplicantId(applicantId) }.returns(listOf())
        every { applicationFormRepository.save(any()) } returns mockk()

        assertDoesNotThrow { applicationFormService.create(applicantId, createApplicationFormRequest) }
    }

    @Test
    fun `기수가 같은 지원 내역이 없는 경우, 지원 가능하다`() {
        val recruitmentOld = createRecruitment(title = "3기 백엔드 모집", term = 3L, id = 1L)
        val recruitmentNew = createRecruitment(title = "4기 백엔드 모집", term = 4L, id = 2L)

        assertDoesNotThrow { createApplicantFormWithHistory(recruitmentOld, recruitmentNew) }
    }

    @Test
    fun `기수가 같은 지원 내역이 이미 있는 경우 지원할 수 없다`() {
        val recruitmentOld = createRecruitment(title = "4기 백엔드 모집", term = 4L, id = 1L)
        val recruitmentNew = createRecruitment(title = "4기 프론트엔드 모집", term = 4L, id = 2L)

        val message = assertThrows<IllegalArgumentException> {
            createApplicantFormWithHistory(recruitmentNew, recruitmentOld)
        }.message

        assertThat(message).isEqualTo("해당 기수에 이미 지원한 이력이 있습니다.")
    }

    @Test
    fun `모집에 기수 정보가 없는 경우에는 이전 지원 내역과 상관없이 지원 가능하다`() {
        val recruitmentOld = createRecruitment(title = "4기 백엔드 모집", id = 1L)
        val recruitmentNew = createRecruitment(title = "4기 프론트엔드 모집", id = 2L)

        assertDoesNotThrow { createApplicantFormWithHistory(recruitmentOld, recruitmentNew) }
    }

    private fun createApplicantFormWithHistory(recruitmentOld: Recruitment, recruitmentNew: Recruitment) {
        every { recruitmentRepository.findByIdOrNull(recruitmentNew.id) } returns recruitmentNew
        every { recruitmentRepository.findByIdOrNull(recruitmentOld.id) } returns recruitmentOld

        every { applicationFormRepository.findAllByApplicantId(applicantId) }.returns(
            listOf(
                ApplicationForm(applicantId, recruitmentOld.id)
            )
        )

        every { recruitmentRepository.findTermById(recruitmentNew.id) } returns recruitmentNew.term
        every { recruitmentRepository.findTermById(recruitmentOld.id) } returns recruitmentOld.term

        every { applicationFormRepository.save(any()) } returns mockk()

        applicationFormService.create(applicantId, CreateApplicationFormRequest(recruitmentNew.id))
    }

    @Test
    fun `지원서가 있으면 지원서를 불러온다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        assertThat(applicationFormService.getApplicationForm(applicantId, 1L)).isEqualTo(applicationFormResponse)
    }

    @Test
    fun `지원서가 없으면 예외를 던진다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        val message =
            assertThrows<IllegalArgumentException> { applicationFormService.getApplicationForm(1L, 1L) }.message
        assertThat(message).isEqualTo("해당하는 지원서가 없습니다.")
    }

    @Test
    fun `지원자가 자신의 지원서를 모두 불러온다`() {
        every { applicationFormRepository.findAllByApplicantId(applicantId) } returns applicationForms

        val expected = applicationFormService.getMyApplicationForms(applicantId)

        assertAll(
            { assertThat(expected).isNotNull },
            { assertThat(expected).hasSize(2) }
        )
    }

    @Test
    fun `지원자가 지원한 지원서가 없으면 빈 리스트를 불러온다`() {
        every { applicationFormRepository.findAllByApplicantId(applicantId) } returns emptyList()

        val expected = applicationFormService.getMyApplicationForms(applicantId)

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

        assertDoesNotThrow { applicationFormService.create(applicantId, createApplicationFormRequest) }
    }

    @Test
    fun `모집이 없는 경우 지원할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns null

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.create(
                applicantId,
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

        assertDoesNotThrow { applicationFormService.update(applicantId, updateApplicationFormRequest) }
    }

    @Test
    fun `지원서가 없는 경우 수정할 수 없다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByApplicantIdAndSubmittedTrue(any()) } returns false
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                applicantId,
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
                applicantId,
                updateApplicationFormRequest
            )
        }.message
        assertThat(message).isEqualTo("지원 불가능한 모집입니다.")
    }

    @Test
    fun `제출한 지원서를 열람할 수 없다`() {
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationFormSubmitted

        val message = assertThrows<IllegalStateException> {
            applicationFormService.getApplicationForm(
                applicantId = 3L,
                recruitmentId = 1L
            )
        }.message
        assertThat(message).isEqualTo("이미 제출한 지원서는 열람할 수 없습니다.")
    }

    @Test
    fun `단 하나의 지원서만 제출할 수 있다`() {
        every { recruitmentRepository.findByIdOrNull(any()) } returns recruitment
        every { recruitmentItemRepository.findByRecruitmentIdOrderByPosition(any()) } returns recruitmentItems
        every { applicationFormRepository.existsByApplicantIdAndSubmittedTrue(any()) } returns true
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns applicationForm1

        val message = assertThrows<IllegalArgumentException> {
            applicationFormService.update(
                applicantId,
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
                applicantId,
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
                applicantId,
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
