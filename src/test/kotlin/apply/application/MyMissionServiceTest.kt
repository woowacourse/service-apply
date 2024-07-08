package apply.application

import apply.createAssignment
import apply.createEvaluationTarget
import apply.createMission
import apply.createMember
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
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

    Given("공개 상태의 과제가 있는 경우") {
        val participatedUser = createMember(id = 1L)
        val notParticipatedUser = createMember(id = 2L)
        val mission = createMission()
        val target = createEvaluationTarget(evaluationId = mission.evaluationId, memberId = participatedUser.id)

        every { missionRepository.getOrThrow(any()) } returns mission
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), eq(participatedUser.id)) } returns target
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), neq(participatedUser.id)) } returns null
        every { assignmentRepository.findByMemberIdAndMissionId(eq(participatedUser.id), any()) } returns createAssignment()

        When("요청한 유저가 과제 참여자이면") {
            val actual = myMissionService.findByUserIdAndMissionId(participatedUser.id, mission.id)

            Then("과제의 상세 내용을 확인할 수 있다") {
                actual.shouldNotBeNull()
                actual.description.shouldNotBeNull()
                actual.submitted.shouldBeTrue()
            }
        }

        When("요청한 유저가 과제 참여자가 아니면") {
            Then("과제의 상세 내용을 확인할 수 없다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByUserIdAndMissionId(notParticipatedUser.id, mission.id)
                }
            }
        }
    }

    Given("비공개 상태의 과제가 있는 경우") {
        val participatedUser = createMember(id = 1L)
        val notParticipatedUser = createMember(id = 2L)
        val mission = createMission(hidden = true)
        val target = createEvaluationTarget(evaluationId = mission.evaluationId, memberId = participatedUser.id)

        every { missionRepository.getOrThrow(any()) } returns mission
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), eq(participatedUser.id)) } returns target
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), neq(participatedUser.id)) } returns null

        When("요청한 유저가 과제 참여자이면") {
            Then("과제 상태로 인한 예외로 상세 내용을 확인할 수 없다") {
                shouldThrow<IllegalStateException> {
                    myMissionService.findByUserIdAndMissionId(participatedUser.id, mission.id)
                }
            }
        }

        When("요청한 유저가 과제 참여자가 아니면") {
            Then("참여 대상자가 아니기 때문에 발생한 예외로 상세 내용을 확인할 수 없다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByUserIdAndMissionId(notParticipatedUser.id, mission.id)
                }
            }
        }
    }
})
