package apply.application

import apply.EVALUATION_ID
import apply.createApplicant
import apply.createEvaluationTarget
import apply.domain.applicant.ApplicantRepository
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.PENDING
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import apply.domain.evaluationtarget.EvaluationTargetRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import support.test.UnitTest

@UnitTest
class MailTargetServiceTest {
    @MockK
    private lateinit var evaluationTargetRepository: EvaluationTargetRepository

    @MockK
    private lateinit var applicantRepository: ApplicantRepository

    private lateinit var mailTargetService: MailTargetService

    @BeforeEach
    internal fun setUp() {
        mailTargetService = MailTargetService(evaluationTargetRepository, applicantRepository)
    }

    @Test
    fun `평가 상태에 따라 (null, 전체) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationId(any()) } returns listOf(
            createEvaluationTarget(applicantId = 1L, evaluationStatus = WAITING),
            createEvaluationTarget(applicantId = 2L, evaluationStatus = PENDING),
            createEvaluationTarget(applicantId = 3L, evaluationStatus = PASS),
            createEvaluationTarget(applicantId = 4L, evaluationStatus = FAIL)
        )
        every { applicantRepository.findAllById(any()) } returns listOf(
            createApplicant(id = 1L, email = "waiting@email.com"),
            createApplicant(id = 2L, email = "pending@email.com"),
            createApplicant(id = 3L, email = "pass@email.com"),
            createApplicant(id = 4L, email = "fail@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID)
        assertThat(actual).hasSize(4)
    }

    @Test
    fun `평가 상태에 따라 (PASS) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(applicantId = 3L, evaluationStatus = PASS)
        )
        every { applicantRepository.findAllById(any()) } returns listOf(
            createApplicant(id = 3L, email = "pass@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID, PASS)
        assertThat(actual).hasSize(1)
        assertThat(actual[0].email).isEqualTo("pass@email.com")
    }

    @Test
    fun `평가 상태에 따라 (FAIL) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(applicantId = 2L, evaluationStatus = FAIL)
        )
        every { applicantRepository.findAllById(any()) } returns listOf(
            createApplicant(id = 2L, email = "fail@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID, FAIL)
        assertThat(actual).hasSize(1)
        assertThat(actual[0].email).isEqualTo("fail@email.com")
    }

    @Test
    fun `평가 상태에 따라 (평가전) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(applicantId = 1L, evaluationStatus = WAITING)
        )
        every { applicantRepository.findAllById(any()) } returns listOf(
            createApplicant(id = 1L, email = "waiting@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID, WAITING)
        assertThat(actual).hasSize(1)
        assertThat(actual[0].email).isEqualTo("waiting@email.com")
    }
}
