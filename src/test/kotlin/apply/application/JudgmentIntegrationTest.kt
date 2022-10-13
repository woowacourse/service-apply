package apply.application

import apply.createAssignment
import apply.createCommit
import apply.createJudgmentItem
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentStatus
import apply.domain.judgment.JudgmentType
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import support.test.IntegrationTest
import support.test.spec.afterRootTest

@MockkBean(AssignmentArchive::class)
@IntegrationTest
class JudgmentIntegrationTest(
    private val judgmentService: JudgmentService,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgmentRepository: JudgmentRepository,
    private val assignmentArchive: AssignmentArchive
) : BehaviorSpec({
    Given("과제 제출물을 제출할 수 있는 특정 과제에 대한 과제 제출물이 있는 경우") {
        val userId = 1L
        val mission = missionRepository.save(createMission(submittable = true))
        judgmentItemRepository.save(createJudgmentItem(mission.id))
        assignmentRepository.save(createAssignment(userId, mission.id))
        val commit = createCommit()

        every { assignmentArchive.getLastCommit(any(), any()) } returns commit

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            val actual = judgmentService.judgeExample(userId, mission.id)

            Then("마지막 커밋에 대한 자동 채점 기록을 확인할 수 있고 자동 채점이 저장된다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe JudgmentStatus.STARTED
                    passCount shouldBe 0
                    totalCount shouldBe 0
                }
                judgmentRepository.findAll().shouldHaveSize(1)
            }
        }
    }

    Given("특정 과제 제출물에 대한 마지막 커밋이 없어 예외가 발생하는 경우") {
        val userId = 1L
        val mission = missionRepository.save(createMission(submittable = true))
        judgmentItemRepository.save(createJudgmentItem(mission.id))
        val assignment = assignmentRepository.save(createAssignment(userId, mission.id))

        every { assignmentArchive.getLastCommit(any(), any()) } throws RuntimeException()

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생하고 자동 채점이 저장되지 않는다") {
                shouldThrowAny {
                    judgmentService.judgeExample(userId, mission.id)
                }
                judgmentRepository.findAll().shouldBeEmpty()
            }
        }

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            Then("예외가 발생하고 자동 채점이 저장되지 않는다") {
                shouldThrowAny {
                    judgmentService.judgeReal(assignment.id)
                }
                judgmentRepository.findAll().shouldBeEmpty()
            }
        }

        When("해당 과제 제출물의 자동 채점을 실행하면") {
            Then("예외가 발생하고 자동 채점이 저장되지 않는다") {
                shouldThrowAny {
                    judgmentService.judge(mission, assignment, JudgmentType.REAL)
                }
                judgmentRepository.findAll().shouldBeEmpty()
            }
        }
    }

    afterRootTest {
        judgmentRepository.deleteAll()
        assignmentRepository.deleteAll()
        judgmentItemRepository.deleteAll()
        missionRepository.deleteAll()
    }
})
