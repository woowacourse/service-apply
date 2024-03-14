package apply.application

import apply.createEvaluationAnswer
import apply.createEvaluationTarget
import apply.createMember
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus.FAIL
import apply.domain.evaluationtarget.EvaluationStatus.PASS
import apply.domain.evaluationtarget.EvaluationStatus.PENDING
import apply.domain.evaluationtarget.EvaluationStatus.WAITING
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.member.MemberRepository
import apply.domain.member.findAllByEmailIn
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import support.test.spec.afterRootTest

class MailTargetServiceTest : BehaviorSpec({
    val evaluationTargetRepository = mockk<EvaluationTargetRepository>()
    val memberRepository = mockk<MemberRepository>()

    val mailTargetService = MailTargetService(evaluationTargetRepository, memberRepository)

    Given("특정 평가에 대한 평가 대상자가 있는 경우") {
        val evaluationId = 1L
        val users = listOf(
            createMember(id = 1L, email = "waiting@email.com"),
            createMember(id = 2L, email = "pending@email.com"),
            createMember(id = 3L, email = "pass@email.com"),
            createMember(id = 4L, email = "fail@email.com")
        )

        every { evaluationTargetRepository.findAllByEvaluationId(any()) } returns listOf(
            createEvaluationTarget(evaluationId = evaluationId, memberId = 1L, evaluationStatus = WAITING),
            createEvaluationTarget(evaluationId = evaluationId, memberId = 2L, evaluationStatus = PENDING),
            createEvaluationTarget(evaluationId = evaluationId, memberId = 3L, evaluationStatus = PASS),
            createEvaluationTarget(evaluationId = evaluationId, memberId = 4L, evaluationStatus = FAIL)
        )
        every { memberRepository.findAllById(any()) } returns users

        When("해당 평가의 모든 평가 대상자에 대한 이메일 정보를 조회하면") {
            val actual = mailTargetService.findMailTargets(evaluationId)

            Then("모든 평가 대상자의 이름 및 이메일을 확인할 수 있다") {
                actual shouldHaveSize 4
                actual.map { it.name to it.email } shouldContainExactlyInAnyOrder users.map { it.name to it.email }
            }
        }
    }

    Given("특정 평가에 합격한 평가 대상자가 있는 경우") {
        val evaluationId = 1L
        val user = createMember(id = 3L, email = "pass@email.com")

        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(evaluationId = evaluationId, memberId = user.id, evaluationStatus = PASS)
        )
        every { memberRepository.findAllById(any()) } returns listOf(user)

        When("해당 평가에 합격한 모든 평가 대상자의 이메일 정보를 조회하면") {
            val actual = mailTargetService.findMailTargets(evaluationId, PASS)

            Then("평가 대상자의 이름 및 이메일을 확인할 수 있다") {
                actual shouldHaveSize 1
                actual[0] shouldBe MailTargetResponse(user.email, user.name)
            }
        }
    }

    Given("특정 평가에 탈락한 평가 대상자가 있는 경우") {
        val evaluationId = 1L
        val user = createMember(id = 2L, email = "fail@email.com")

        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(evaluationId = evaluationId, memberId = user.id, evaluationStatus = FAIL)
        )
        every { memberRepository.findAllById(any()) } returns listOf(user)

        When("해당 평가에 탈락한 모든 평가 대상자의 이메일 정보를 조회하면") {
            val actual = mailTargetService.findMailTargets(evaluationId, FAIL)

            Then("평가 대상자의 이름 및 이메일을 확인할 수 있다") {
                actual shouldHaveSize 1
                actual[0] shouldBe MailTargetResponse(user.email, user.name)
            }
        }
    }

    Given("특정 평가에 보류 중인 평가 대상자가 있는 경우") {
        val evaluationId = 1L
        val user = createMember(id = 2L, email = "waiting@email.com")

        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(evaluationId = evaluationId, memberId = user.id, evaluationStatus = WAITING)
        )
        every { memberRepository.findAllById(any()) } returns listOf(user)

        When("해당 평가에 보류 중인 모든 평가 대상자의 이메일 정보를 조회하면") {
            val actual = mailTargetService.findMailTargets(evaluationId, WAITING)

            Then("평가 대상자의 이름 및 이메일을 확인할 수 있다") {
                actual shouldHaveSize 1
                actual[0] shouldBe MailTargetResponse(user.email, user.name)
            }
        }
    }

    Given("특정 평가에 평가를 받지 않고 탈락한 평가 대상자가 있는 경우") {
        val evaluationId = 1L

        every { evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(any(), any()) } returns listOf(
            createEvaluationTarget(
                evaluationId = evaluationId,
                memberId = 1L,
                evaluationStatus = FAIL,
                evaluationAnswers = EvaluationAnswers(emptyList()),
            ),
            createEvaluationTarget(
                evaluationId = evaluationId,
                memberId = 2L,
                evaluationStatus = FAIL,
                evaluationAnswers = EvaluationAnswers(listOf(createEvaluationAnswer(score = 0)))
            )
        )
        every { memberRepository.findAllById(any()) } returns emptyList()

        When("해당 평가에 탈락한 모든 평가 대상자의 이메일 정보를 조회하면") {
            val actual = mailTargetService.findMailTargets(evaluationId, FAIL)

            Then("해당 평가 대상자의 이름 및 이메일을 확인할 수 없다") {
                verify { memberRepository.findAllById(emptyList()) }
                actual.shouldBeEmpty()
            }
        }
    }

    Given("특정 이메일을 가진 회원이 없는 경우") {
        val email = "test1@email.com"

        every { memberRepository.findAllByEmailIn(any()) } returns emptyList()

        When("해당 이메일로 이메일 정보를 조회하면") {
            val actual = mailTargetService.findAllByEmails(listOf(email))

            Then("이름이 비어있는 것을 확인할 수 있다") {
                actual[0] shouldBe MailTargetResponse(email, null)
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
