package apply.application

import apply.COMMIT_HASH
import apply.PULL_REQUEST_URL
import apply.createAssignment
import apply.createCommit
import apply.createJudgment
import apply.createJudgmentRecord
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.assignment.getOrThrow
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus.STARTED
import apply.domain.judgment.JudgmentStatus.SUCCEEDED
import apply.domain.judgment.JudgmentType.EXAMPLE
import apply.domain.judgment.JudgmentType.REAL
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getOrThrow
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest
import java.time.LocalDateTime.now

class JudgmentServiceTest : BehaviorSpec({
    val judgmentRepository = mockk<JudgmentRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val missionRepository = mockk<MissionRepository>()
    val judgmentItemRepository = mockk<JudgmentItemRepository>()
    val assignmentArchive = mockk<AssignmentArchive>()

    val judgmentService = JudgmentService(
        judgmentRepository,
        assignmentRepository,
        missionRepository,
        judgmentItemRepository,
        assignmentArchive
    )

    Given("과제 제출물을 제출할 수 없는 과제가 있는 경우") {
        val mission = createMission(submittable = false, id = 1L)

        every { missionRepository.getOrThrow(any()) } returns mission

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, mission.id)
                }
            }
        }
    }

    Given("과제 제출물을 제출할 수 없는 과제에 대한 과제 제출물이 있는 경우") {
        val mission = createMission(submittable = false, id = 1L)
        val assignment = createAssignment(missionId = mission.id, url = PULL_REQUEST_URL, id = 1L)

        every { assignmentRepository.getOrThrow(any()) } returns assignment
        every { missionRepository.getOrThrow(any()) } returns mission
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns null
        every { judgmentRepository.save(any()) } answers { firstArg() }
        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns createCommit()

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(assignment.id)

            Then("자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    url shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe STARTED
                }
            }
        }
    }

    Given("자동 채점 항목이 없는 특정 과제에 대한 과제 제출물이 있는 경우") {
        val mission = createMission(submittable = true, id = 1L)
        val assignment = createAssignment(missionId = mission.id, url = PULL_REQUEST_URL, id = 1L)

        every { assignmentRepository.getOrThrow(any()) } returns assignment
        every { missionRepository.getOrThrow(any()) } returns mission
        every { judgmentItemRepository.existsByMissionId(any()) } returns false

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, mission.id)
                }
            }
        }

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeReal(assignment.id)
                }
            }
        }
    }

    Given("특정 과제의 과제 제출물에 대한 예제 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 같은 경우") {
        val mission = createMission(submittable = true, id = 1L)
        val assignment = createAssignment(missionId = mission.id, id = 1L)
        val commit = createCommit()
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = EXAMPLE,
            records = listOf(
                createJudgmentRecord(
                    commit = commit,
                    result = JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
                    completedDateTime = now()
                )
            )
        )

        every { missionRepository.getOrThrow(any()) } returns mission
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns createCommit()
        every { judgmentRepository.save(any()) } answers { firstArg() }

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            val actual = judgmentService.judgeExample(1L, mission.id)

            Then("동일한 자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe SUCCEEDED
                    passCount shouldBe 9
                    totalCount shouldBe 10
                }
            }
        }
    }

    Given("특정 과제의 과제 제출물에 대한 본 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 같은 경우") {
        val mission = createMission(submittable = true, id = 1L)
        val assignment = createAssignment(missionId = mission.id, id = 1L)
        val commit = createCommit()
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = REAL,
            records = listOf(
                createJudgmentRecord(
                    commit = commit,
                    result = JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
                    completedDateTime = now()
                )
            )
        )

        every { assignmentRepository.getOrThrow(any()) } returns assignment
        every { missionRepository.getOrThrow(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(assignment.id)

            Then("동일한 자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe SUCCEEDED
                    passCount shouldBe 9
                    totalCount shouldBe 10
                }
            }
        }
    }

    Given("특정 과제의 과제 제출물에 대한 예제 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 다른 경우") {
        val mission = createMission(submittable = true, id = 1L)
        val assignment = createAssignment(missionId = mission.id, id = 1L)
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = EXAMPLE,
            records = listOf(
                createJudgmentRecord(
                    commit = createCommit("commit1"),
                    startedDateTime = now().minusMinutes(5)
                )
            )
        )
        val commit = createCommit("commit2")

        every { missionRepository.getOrThrow(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            val actual = judgmentService.judgeExample(1L, mission.id)

            Then("최신 커밋에 대한 자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe STARTED
                    passCount shouldBe 0
                    totalCount shouldBe 0
                }
            }
        }
    }

    Given("특정 과제의 과제 제출물에 대한 본 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 다른 경우") {
        val mission = createMission(id = 1L, submittable = true)
        val assignment = createAssignment(missionId = mission.id, id = 1L)
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = REAL,
            records = listOf(
                createJudgmentRecord(
                    commit = createCommit("commit1"),
                    startedDateTime = now().minusMinutes(5)
                )
            )
        )
        val commit = createCommit("commit2")

        every { assignmentRepository.getOrThrow(any()) } returns assignment
        every { missionRepository.getOrThrow(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(assignment.id)

            Then("최신 커밋에 대한 자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    commitHash shouldBe commit.hash
                    status shouldBe STARTED
                    passCount shouldBe 0
                    totalCount shouldBe 0
                }
            }
        }
    }

    Given("특정 과제 제출물에 대한 예제 자동 채점 기록이 있는 경우") {
        val assignment = createAssignment(url = PULL_REQUEST_URL)
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = EXAMPLE,
            records = listOf(
                createJudgmentRecord(
                    commit = createCommit(COMMIT_HASH),
                    result = JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
                    completedDateTime = now()
                )
            )
        )

        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment

        When("예제 자동 채점 결과를 조회하면") {
            val actual = judgmentService.findLastExampleJudgment(1L, 1L)

            Then("예제 자동 채점 결과를 확인할 수 있다") {
                actual.shouldNotBeNull()
                assertSoftly(actual) {
                    commitHash shouldBe COMMIT_HASH
                    passCount shouldBe 9
                    totalCount shouldBe 10
                    status shouldBe SUCCEEDED
                }
            }
        }
    }

    Given("특정 과제 제출물에 대한 본 자동 채점 기록이 있는 경우") {
        val assignment = createAssignment(url = PULL_REQUEST_URL)
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = REAL,
            records = listOf(
                createJudgmentRecord(
                    commit = createCommit(COMMIT_HASH),
                    result = JudgmentResult(passCount = 9, totalCount = 10, status = SUCCEEDED),
                    completedDateTime = now()
                )
            )
        )

        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment

        When("본 자동 채점 결과를 조회하면") {
            val actual = judgmentService.findLastExampleJudgment(1L, 1L)

            Then("본 자동 채점 결과를 확인할 수 있다") {
                actual.shouldNotBeNull()
                assertSoftly(actual) {
                    commitHash shouldBe COMMIT_HASH
                    passCount shouldBe 9
                    totalCount shouldBe 10
                    status shouldBe SUCCEEDED
                }
            }
        }
    }

    Given("특정 과제 제출물에 대한 예제 자동 채점 기록이 없는 경우") {
        val assignment = createAssignment(url = PULL_REQUEST_URL)

        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns null

        When("예제 자동 채점 결과를 조회하면") {
            val actual = judgmentService.findLastExampleJudgment(1L, 1L)

            Then("null을 반환한다") {
                actual.shouldBeNull()
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
