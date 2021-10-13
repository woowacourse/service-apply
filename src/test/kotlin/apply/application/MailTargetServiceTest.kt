package apply.application

import apply.EVALUATION_ID
import apply.createEvaluationAnswer
import apply.createEvaluationTarget
import apply.createUser
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.PENDING
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.user.UserRepository
import apply.domain.user.findAllByEmailIn
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
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
    fun `평가 상태에 따라 (FAIL) 메일 발송 시 과제 미제출자는 대상이 되지 않는다`() {
        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(
                userId = 1L,
                evaluationStatus = FAIL,
                evaluationAnswers = EvaluationAnswers(emptyList())
            ),
            createEvaluationTarget(
                userId = 2L,
                evaluationStatus = FAIL,
                evaluationAnswers = EvaluationAnswers(listOf(createEvaluationAnswer(score = 0)))
            )
        )
        every { userRepository.findAllById(any()) } returns emptyList()
        mailTargetService.findMailTargets(EVALUATION_ID, FAIL)
        verify { userRepository.findAllById(emptyList()) }
    }

    @Test
    fun `평가 상태에 따라 (WAITING) 메일 발송 대상들의 이메일 정보를 불러온다`() {
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

    @Test
    fun `이메일 중에서 회원에 해당하는 이메일이 있으면 (회원이름, 이메일)을, 회원에 해당하는 이메일이 없으면 (공백, 이메일)을 반환한다`() {
        val users = listOf(
            createUser(name = "회원1", email = "test1@email.com"),
            createUser(name = "회원3", email = "test3@email.com")
        )
        val emails = listOf("test1@email.com", "test2@email.com", "test3@email.com")
        val mailTargetResponses = listOf(
            MailTargetResponse("test1@email.com", "회원1"),
            MailTargetResponse("test3@email.com", "회원3"),
            MailTargetResponse("test2@email.com")
        )
        every { userRepository.findAllByEmailIn(emails) } returns users
        val actual = mailTargetService.findAllByEmails(emails)
        assertThat(actual).isEqualTo(mailTargetResponses)
    }
}
