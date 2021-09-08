package apply.application

import apply.EVALUATION_ID
import apply.createApplicant
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import support.test.RepositoryTest

@RepositoryTest
class MailSendingServiceTest {

    @MockK
    private lateinit var mailSendingService: MailSendingService

    @Test
    fun `평가 상태에따라 (null, 전체) 메일 발송 대상들의 이메일 정보를 불러온다`() { // todo: 테스트 바껴야함!
        val passApplicant = createApplicant(id = 1L, email = "a@woowacourse.com")
        val failApplicant = createApplicant(id = 2L, email = "b@woowacourse.com")
        val notSubmitAssignmentApplicant = createApplicant(id = 3L, email = "c@woowacourse.com")

        every {
            mailSendingService.findMailSendingTargetsByEvaluationStatus(EVALUATION_ID, null)
        } returns listOf(passApplicant, failApplicant, notSubmitAssignmentApplicant)
            .map { MailSendingTargetResponse(it.email) }

        val mailSendingTargets =
            mailSendingService.findMailSendingTargetsByEvaluationStatus(EVALUATION_ID, null)

        Assertions.assertThat(mailSendingTargets).hasSize(3)
    }

    @Test
    fun `평가 상태에따라 (PASS) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        val passApplicant = createApplicant(id = 1L, email = "a@woowacourse.com")

        every {
            mailSendingService.findMailSendingTargetsByEvaluationStatus(EVALUATION_ID, PASS)
        } returns listOf(passApplicant)
            .map { MailSendingTargetResponse(it.email) }

        val mailSendingTargetEmails = mailSendingService.findMailSendingTargetsByEvaluationStatus(
            EVALUATION_ID, PASS
        )

        Assertions.assertThat(mailSendingTargetEmails).hasSize(1)
    }

    @Test
    fun `평가 상태에따라 (FAIL) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        val failApplicant = createApplicant(id = 2L, email = "b@woowacourse.com")

        every {
            mailSendingService.findMailSendingTargetsByEvaluationStatus(EVALUATION_ID, FAIL)
        } returns listOf(failApplicant)
            .map { MailSendingTargetResponse(it.email) }

        val mailSendingTargetEmails = mailSendingService.findMailSendingTargetsByEvaluationStatus(
            EVALUATION_ID, FAIL
        )

        Assertions.assertThat(mailSendingTargetEmails).hasSize(1)
    }

    @Test
    fun `평가 상태에따라 (평가전) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        val firstApplicant = createApplicant(id = 1L, email = "a@woowacourse.com")
        val secondApplicant = createApplicant(id = 2L, email = "b@woowacourse.com")
        val thirdApplicant = createApplicant(id = 3L, email = "c@woowacourse.com")

        every {
            mailSendingService.findMailSendingTargetsByEvaluationStatus(EVALUATION_ID, WAITING)
        } returns listOf(firstApplicant, secondApplicant, thirdApplicant)
            .map { MailSendingTargetResponse(it.email) }

        val mailSendingTargetEmails = mailSendingService.findMailSendingTargetsByEvaluationStatus(
            EVALUATION_ID, WAITING
        )

        Assertions.assertThat(mailSendingTargetEmails).hasSize(3)
    }
}
