package apply.application

import apply.createAssignment
import apply.createAssignmentRequest
import apply.createEvaluationTarget
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest

class AssignmentServiceTest : BehaviorSpec({
    val assignmentRepository = mockk<AssignmentRepository>()
    val missionRepository = mockk<MissionRepository>()
    val evaluationTargetRepository = mockk<EvaluationTargetRepository>()

    val assignmentService = AssignmentService(assignmentRepository, missionRepository, evaluationTargetRepository)

    Given("제출할 수 있는 과제에 대해 과제 제출물을 제출하지 않은 특정 평가 대상자가 있는 경우") {
        val mission = createMission(submittable = true)
        val evaluationTarget = createEvaluationTarget(memberId = 1L)

        every { assignmentRepository.existsByMemberIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getOrThrow(any()) } returns mission
        every { evaluationTargetRepository.findByEvaluationIdAndMemberId(any(), any()) } returns evaluationTarget
        every { assignmentRepository.save(any()) } returns createAssignment()

        When("특정 평가 대상자가 과제 제출물을 생성하면") {
            Then("과제 제출물을 생성할 수 있다") {
                shouldNotThrowAny {
                    assignmentService.create(mission.id, evaluationTarget.memberId, createAssignmentRequest())
                }
            }
        }
    }

    Given("제출이 불가능한 과제에 대해 과제 제출물을 제출하지 않은 특정 평가 대상자가 있는 경우") {
        val mission = createMission(submittable = false)
        val evaluationTarget = createEvaluationTarget(memberId = 1L)

        every { assignmentRepository.existsByMemberIdAndMissionId(any(), any()) } returns false
        every { missionRepository.getOrThrow(any()) } returns mission

        When("특정 평가 대상자가 과제 제출물을 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    assignmentService.create(mission.id, evaluationTarget.memberId, createAssignmentRequest())
                }
            }
        }
    }

    Given("특정 평가 대상자가 특정 과제에 대한 과제 제출물을 이미 생성한 경우") {
        every { assignmentRepository.existsByMemberIdAndMissionId(any(), any()) } returns true

        When("특정 평가 대상자가 과제 제출물을 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    assignmentService.create(1L, 1L, createAssignmentRequest())
                }
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
