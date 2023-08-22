package apply.application

import apply.createAssignment
import apply.createCommit
import apply.createJudgmentItem
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getOrThrow
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.judgment.JudgmentType.EXAMPLE
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.judgmentitem.getByMissionId
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk

class JudgmentRequestServiceTest : BehaviorSpec({
    val judgmentItemRepository = mockk<JudgmentItemRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val judgmentAgency = mockk<JudgmentAgency>()

    val judgmentRequestService = JudgmentRequestService(judgmentItemRepository, assignmentRepository, judgmentAgency)

    Given("특정 과제에 대한 과제 제출물 및 자동 채점 항목이 있는 경우") {
        val missionId = 1L
        val assignment = createAssignment(missionId = missionId, id = 1L)
        val judgmentItem = createJudgmentItem(missionId = missionId)

        every { assignmentRepository.getOrThrow(any()) } returns assignment
        every { judgmentItemRepository.getByMissionId(any()) } returns judgmentItem
        every { judgmentAgency.requestJudge(any()) } just Runs

        When("자동 채점을 요청하면") {
            Then("자동 채점을 요청할 수 있다") {
                shouldNotThrowAny {
                    judgmentRequestService.request(
                        JudgmentStartedEvent(1L, assignment.id, EXAMPLE, createCommit())
                    )
                }
            }
        }
    }
})
