package apply.application

import apply.createAssignment
import apply.createCancelJudgmentRequest
import apply.createCommit
import apply.createFailJudgmentRequest
import apply.createJudgment
import apply.createJudgmentItem
import apply.createJudgmentRecord
import apply.createMission
import apply.createSuccessJudgmentRequest
import apply.domain.assignment.AssignmentRepository
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.JudgmentCancelledEvent
import apply.domain.judgment.JudgmentFailedEvent
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStartedEvent
import apply.domain.judgment.JudgmentStatus.CANCELLED
import apply.domain.judgment.JudgmentStatus.FAILED
import apply.domain.judgment.JudgmentStatus.STARTED
import apply.domain.judgment.JudgmentStatus.SUCCEEDED
import apply.domain.judgment.JudgmentSucceededEvent
import apply.domain.judgment.JudgmentTouchedEvent
import apply.domain.judgment.JudgmentType.EXAMPLE
import apply.domain.judgment.JudgmentType.REAL
import apply.domain.judgment.getOrThrow
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
import org.springframework.context.annotation.Import
import support.test.IntegrationTest
import support.test.context.event.Events
import support.test.context.event.RecordEventsConfiguration
import support.test.spec.afterRootTest
import java.time.LocalDateTime.now

@MockkBean(value = [AssignmentArchive::class, JudgmentAgency::class], relaxUnitFun = true)
@Import(RecordEventsConfiguration::class)
@IntegrationTest
class JudgmentIntegrationTest(
    private val judgmentService: JudgmentService,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgmentRepository: JudgmentRepository,
    private val assignmentArchive: AssignmentArchive,
    private val events: Events
) : BehaviorSpec({
    Given("과제 제출물을 제출할 수 있는 특정 과제에 대한 과제 제출물이 있는 경우") {
        val userId = 1L
        val mission = missionRepository.save(createMission(submittable = true))
        judgmentItemRepository.save(createJudgmentItem(mission.id))
        assignmentRepository.save(createAssignment(userId, mission.id))
        val commit = createCommit()

        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns commit

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            val actual = judgmentService.judgeExample(userId, mission.id)

            Then("마지막 커밋에 대한 자동 채점 기록을 확인할 수 있고 자동 채점이 저장된다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe STARTED
                    passCount shouldBe 0
                    totalCount shouldBe 0
                }
                events.count<JudgmentStartedEvent>() shouldBe 1
                judgmentRepository.findAll().shouldHaveSize(1)
            }
        }
    }

    Given("특정 과제 제출물에 대한 마지막 커밋이 없어 예외가 발생하는 경우") {
        val userId = 1L
        val mission = missionRepository.save(createMission(submittable = true))
        judgmentItemRepository.save(createJudgmentItem(mission.id))
        val assignment = assignmentRepository.save(createAssignment(userId, mission.id))

        every { assignmentArchive.getLastCommit(any(), any(), any()) } throws RuntimeException()

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
                    judgmentService.judge(mission, assignment, REAL)
                }
                judgmentRepository.findAll().shouldBeEmpty()
            }
        }
    }

    Given("특정 과제 제출물에 대한 예제 자동 채점 기록이 있고 이전 커밋과 마지막 커밋이 동일한 경우") {
        val userId = 1L
        val mission = missionRepository.save(createMission(submittable = true))
        judgmentItemRepository.save(createJudgmentItem(mission.id))
        val assignment = assignmentRepository.save(createAssignment(userId, mission.id))
        val commit = createCommit()
        judgmentRepository.save(
            createJudgment(
                assignment.id, EXAMPLE,
                listOf(
                    createJudgmentRecord(
                        commit,
                        JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
                        completedDateTime = now()
                    )
                )
            )
        )

        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns commit

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            val actual = judgmentService.judgeExample(userId, mission.id)

            Then("해당 커밋에 대한 자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe SUCCEEDED
                    passCount shouldBe 9
                    totalCount shouldBe 10
                }
                events.count<JudgmentStartedEvent>() shouldBe 0
                events.count<JudgmentTouchedEvent>() shouldBe 1
            }
        }
    }

    Given("특정 과제 제출물에 대한 본 자동 채점 기록이 있고 이전 커밋과 마지막 커밋이 동일한 경우") {
        val mission = missionRepository.save(createMission())
        judgmentItemRepository.save(createJudgmentItem(mission.id))
        val assignment = assignmentRepository.save(createAssignment(1L, mission.id))
        val commit = createCommit()
        judgmentRepository.save(
            createJudgment(
                assignment.id, REAL,
                listOf(
                    createJudgmentRecord(
                        commit,
                        JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
                        completedDateTime = now()
                    )
                )
            )
        )

        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns commit

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(assignment.id)

            Then("해당 커밋에 대한 자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe SUCCEEDED
                    passCount shouldBe 9
                    totalCount shouldBe 10
                }
                events.count<JudgmentStartedEvent>() shouldBe 0
                events.count<JudgmentTouchedEvent>() shouldBe 1
            }
        }
    }

    Given("특정 예제 자동 채점에 특정 커밋에 대한 자동 채점 기록이 있는 경우") {
        val assignment = assignmentRepository.save(createAssignment(1L, 1L))
        val commit = createCommit()
        val judgment = judgmentRepository.save(
            createJudgment(assignment.id, EXAMPLE, listOf(createJudgmentRecord(commit)))
        )

        When("해당 커밋의 자동 채점이 성공하면") {
            judgmentService.success(
                judgment.id,
                createSuccessJudgmentRequest(commit.hash, passCount = 9, totalCount = 10)
            )

            Then("자동 채점 기록의 상태가 성공이 된다") {
                val actual = judgmentRepository.getOrThrow(judgment.id)
                actual.lastRecord.result shouldBe JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED)
                events.count<JudgmentSucceededEvent>() shouldBe 1
            }
        }
    }

    Given("특정 본 자동 채점의 특정 커밋에 대한 자동 채점 기록이 있는 경우") {
        val assignment = assignmentRepository.save(createAssignment(1L, 1L))
        val commit = createCommit()
        val judgment = judgmentRepository.save(
            createJudgment(assignment.id, REAL, listOf(createJudgmentRecord(commit)))
        )

        When("해당 커밋의 자동 채점이 성공하면") {
            judgmentService.success(
                judgment.id,
                createSuccessJudgmentRequest(commit.hash, passCount = 9, totalCount = 10)
            )

            Then("자동 채점 기록의 상태가 성공이 된다") {
                val actual = judgmentRepository.getOrThrow(judgment.id)
                actual.lastRecord.result shouldBe JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED)
                events.count<JudgmentSucceededEvent>() shouldBe 1
            }
        }

        When("해당 커밋의 자동 채점이 실패하면") {
            judgmentService.fail(judgment.id, createFailJudgmentRequest(commit.hash))

            Then("자동 채점 기록의 상태가 실패가 된다") {
                val actual = judgmentRepository.getOrThrow(judgment.id)
                actual.lastRecord.result.status shouldBe FAILED
                events.count<JudgmentFailedEvent>() shouldBe 1
            }
        }

        When("해당 커밋의 자동 채점이 취소되면") {
            judgmentService.cancel(judgment.id, createCancelJudgmentRequest(commit.hash))

            Then("자동 채점 기록의 상태가 취소가 된다") {
                val actual = judgmentRepository.getOrThrow(judgment.id)
                actual.lastRecord.result.status shouldBe CANCELLED
                events.count<JudgmentCancelledEvent>() shouldBe 1
            }
        }
    }

    afterEach {
        events.clear()
    }

    afterRootTest {
        judgmentRepository.deleteAll()
        assignmentRepository.deleteAll()
        judgmentItemRepository.deleteAll()
        missionRepository.deleteAll()
    }
})
