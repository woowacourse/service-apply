package apply.application

import apply.createAssignment
import apply.createCommit
import apply.createJudgmentItem
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getById
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.judgment.JudgmentType
import apply.domain.mission.JudgmentItemRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk

class JudgmentRequestServiceTest : BehaviorSpec({
    val judgmentItemRepository = mockk<JudgmentItemRepository>()
    val judgmentAgency = mockk<JudgmentAgency>()
    val assignmentRepository = mockk<AssignmentRepository>()

    val judgmentRequestService = JudgmentRequestService(judgmentItemRepository, judgmentAgency, assignmentRepository)

    Given("자동 채점이 시작된 경우") {
        every { assignmentRepository.getById(any()) } returns createAssignment()
        every { judgmentItemRepository.findByMissionId(any()) } returns createJudgmentItem()
        every { judgmentAgency.requestJudge(any()) } just Runs

        When("자동 채점 실행 요청을 보내면") {
            Then("정상적으로 요청을 전송한다") {
                val event = JudgmentStartedEvent(1L, 1L, JudgmentType.EXAMPLE, createCommit())

                shouldNotThrowAny { judgmentRequestService.request(event) }
            }
        }
    }
})
