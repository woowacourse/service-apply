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
    private lateinit var recruimentRepository: RecruitmentRepository

    @MockK
    private lateinit var applicantService: ApplicantService

    private lateinit var applicationFormService: ApplicationFormService

    private lateinit var applicationForm1: ApplicationForm
    private lateinit var applicationForm2: ApplicationForm
    private lateinit var applicationFormResponse: ApplicationFormResponse
    private lateinit var applicationFormSaveRequest: SaveApplicationFormRequest
    private lateinit var applicationFormUpdateRequest: UpdateApplicationFormRequest

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

        applicationFormSaveRequest = SaveApplicationFormRequest(
            recruitmentId = applicationForm1.recruitmentId,
            referenceUrl = applicationForm1.referenceUrl,
            isSubmitted = false,
            answers = applicationForm1.answers.items.map { AnswerRequest(it.contents, it.recruitmentItemId) }
        )

        applicationFormUpdateRequest = UpdateApplicationFormRequest(
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
    fun `지원서를 최초 저장한다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns false
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()
        assertDoesNotThrow { applicationFormService.save(1L, applicationFormSaveRequest) }
    }

    @Test
    fun `최초 저장 시 지원이 이미 존재하면 오류를 반환한다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every { applicationFormRepository.existsByRecruitmentIdAndApplicantId(any(), any()) } returns true

        assertThatIllegalArgumentException().isThrownBy { applicationFormService.save(1L, applicationFormSaveRequest) }
            .withMessage("이미 저장된 지원서가 있습니다.")
    }

    @Test
    fun `업데이트 시 이미 저장된 지원이 없으면 오류를 반환한다`() {
        every { applicationFormRepository.findByRecruitmentIdAndApplicantId(any(), any()) } returns null
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment

        assertThatIllegalArgumentException().isThrownBy {
            applicationFormService.update(
                1L,
                applicationFormUpdateRequest
            )
        }
            .withMessage("저장된 지원서가 없습니다.")
    }

    @Test
    fun `업데이트 시 저장과 함께 비밀번호를 업데이트 한다`() {
        every { applicantService.changePassword(any(), any()) } returns mockk()
        every { applicationFormRepository.save(any<ApplicationForm>()) } returns mockk()
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitment
        every {
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                any(),
                any()
            )
        } returns applicationForm1

        applicationFormService.update(1L, applicationFormUpdateRequest)
        assertDoesNotThrow { applicationFormRepository.save(applicationForm1) }
        verify { applicantService.changePassword(1L, applicationFormUpdateRequest.password) }
    }

    @Test
    fun `모집이 존재하지 않는 경우 지원을 할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns null

        assertAll(
            { assertThrows<IllegalArgumentException> { applicationFormService.save(1L, applicationFormSaveRequest) } },
            {
                assertThrows<IllegalArgumentException> {
                    applicationFormService.update(
                        1L,
                        applicationFormUpdateRequest
                    )
                }
            }
        )
    }

    @Test
    fun `모집중이 아닌 지원서에 대해 저장 및 수정할 수 없다`() {
        every { recruimentRepository.findByIdOrNull(any()) } returns recruitmentNotRecruiting

        assertAll(
            { assertThrows<IllegalStateException> { applicationFormService.save(1L, applicationFormSaveRequest) } },
            { assertThrows<IllegalStateException> { applicationFormService.update(1L, applicationFormUpdateRequest) } }
        )
    }
}
