package apply.application

import apply.createEvaluationTarget
import apply.createMember
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk

class MyMissionServiceTest : BehaviorSpec({
    val evaluationRepository = mockk<EvaluationRepository>()
    val evaluationTargetRepository = mockk<EvaluationTargetRepository>()
    val missionRepository = mockk<MissionRepository>()
    val judgmentItemRepository = mockk<JudgmentItemRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val judgmentRepository = mockk<JudgmentRepository>()

    val myMissionService = MyMissionService(
        evaluationRepository,
        evaluationTargetRepository,
        missionRepository,
        judgmentItemRepository,
        assignmentRepository,
        judgmentRepository
    )

    Given("과제가 과제 기간 내에 있고 공개인 경우") {
        val mission = createMission(hidden = false)
        val evaluationTarget = createEvaluationTarget(evaluationId = mission.evaluationId, memberId = 1L)
        val justMember = createMember(id = 2L)

        every { missionRepository.getOrThrow(any()) } returns mission
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), evaluationTarget.memberId) } returns evaluationTarget
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), justMember.id) } returns null
        every { assignmentRepository.findByMemberIdAndMissionId(any(), any()) } returns null

        When("평가 대상자가 과제를 조회하면") {
            val actual = myMissionService.findByMemberIdAndMissionId(evaluationTarget.memberId, mission.id)

            Then("과제를 확인할 수 있다") {
                actual.shouldNotBeNull()
                actual.description.shouldNotBeNull()
            }
        }

        When("평가 대상자가 아닌 회원이 과제를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByMemberIdAndMissionId(justMember.id, mission.id)
                }
            }
        }
    }

    Given("과제가 과제 기간 내에 있지만 비공개인 경우") {
        val mission = createMission(hidden = true)
        val evaluationTarget = createEvaluationTarget(evaluationId = mission.evaluationId, memberId = 1L)
        val justMember = createMember(id = 2L)

        every { missionRepository.getOrThrow(any()) } returns mission
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), evaluationTarget.memberId) } returns evaluationTarget
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), justMember.id) } returns null
        every { assignmentRepository.findByMemberIdAndMissionId(any(), any()) } returns null

        When("평가 대상자가 과제를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByMemberIdAndMissionId(evaluationTarget.memberId, mission.id)
                }
            }
        }

        When("평가 대상자가 아닌 회원이 과제를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByMemberIdAndMissionId(justMember.id, mission.id)
                }
            }
        }
    }
})
