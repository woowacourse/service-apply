package apply.application

import apply.EVALUATION_ID
import apply.createEvaluationTarget
import apply.createUser
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.PENDING
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.user.UserRepository
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
    private lateinit var userRepository: UserRepository

    private lateinit var mailTargetService: MailTargetService

    @BeforeEach
    internal fun setUp() {
        mailTargetService = MailTargetService(evaluationTargetRepository, userRepository)
    }

    @Test
    fun `평가 상태에 따라 (null, 전체) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationId(any()) } returns listOf(
            createEvaluationTarget(userId = 1L, evaluationStatus = WAITING),
            createEvaluationTarget(userId = 2L, evaluationStatus = PENDING),
            createEvaluationTarget(userId = 3L, evaluationStatus = PASS),
            createEvaluationTarget(userId = 4L, evaluationStatus = FAIL)
        )
        every { userRepository.findAllById(any()) } returns listOf(
            createUser(id = 1L, email = "waiting@email.com"),
            createUser(id = 2L, email = "pending@email.com"),
            createUser(id = 3L, email = "pass@email.com"),
            createUser(id = 4L, email = "fail@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID)
        assertThat(actual).hasSize(4)
    }

    @Test
    fun `평가 상태에 따라 (PASS) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(userId = 3L, evaluationStatus = PASS)
        )
        every { userRepository.findAllById(any()) } returns listOf(
            createUser(id = 3L, email = "pass@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID, PASS)
        assertThat(actual).hasSize(1)
        assertThat(actual[0].email).isEqualTo("pass@email.com")
    }

    @Test
    fun `평가 상태에 따라 (FAIL) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(userId = 2L, evaluationStatus = FAIL)
        )
        every { userRepository.findAllById(any()) } returns listOf(
            createUser(id = 2L, email = "fail@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID, FAIL)
        assertThat(actual).hasSize(1)
        assertThat(actual[0].email).isEqualTo("fail@email.com")
    }

    @Test
    fun `평가 상태에 따라 (평가전) 메일 발송 대상들의 이메일 정보를 불러온다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(userId = 1L, evaluationStatus = WAITING)
        )
        every { userRepository.findAllById(any()) } returns listOf(
            createUser(id = 1L, email = "waiting@email.com")
        )
        val actual = mailTargetService.findMailTargets(EVALUATION_ID, WAITING)
        assertThat(actual).hasSize(1)
        assertThat(actual[0].email).isEqualTo("waiting@email.com")
    }
}
