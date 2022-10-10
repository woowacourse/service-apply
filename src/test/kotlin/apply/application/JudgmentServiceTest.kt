package apply.application

import apply.COMMIT_HASH
import apply.PULL_REQUEST_URL
import apply.createAssignment
import apply.createCancelJudgmentRequest
import apply.createCommit
import apply.createFailJudgmentRequest
import apply.createJudgment
import apply.createJudgmentRecord
import apply.createMission
import apply.createSuccessJudgmentRequest
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus.CANCELLED
import apply.domain.judgment.JudgmentStatus.FAILED
import apply.domain.judgment.JudgmentStatus.STARTED
import apply.domain.judgment.JudgmentStatus.SUCCEEDED
import apply.domain.judgment.JudgmentType.EXAMPLE
import apply.domain.judgment.JudgmentType.REAL
import apply.domain.judgment.getById
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
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
        judgmentRepository, assignmentRepository, missionRepository, judgmentItemRepository, assignmentArchive
    )

    Given("과제 제출물을 제출할 수 없는 과제가 있는 경우") {
        val mission = createMission(submittable = false, id = 1L)

        every { missionRepository.getById(any()) } returns mission

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
        val assignment = createAssignment(missionId = mission.id, pullRequestUrl = PULL_REQUEST_URL, id = 1L)

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns null
        every { judgmentRepository.save(any()) } returns createJudgment(assignmentId = assignment.id, type = REAL)

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(1L, mission.id)

            Then("자동 채점 기록을 확인할 수 있다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe STARTED
                }
            }
        }
    }

    Given("자동 채점 항목이 없는 특정 과제에 대한 과제 제출물이 있는 경우") {
        val mission = createMission(submittable = true, id = 1L)
        val assignment = createAssignment(missionId = mission.id, pullRequestUrl = PULL_REQUEST_URL, id = 1L)

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
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
                    judgmentService.judgeReal(1L, mission.id)
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

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

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

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(1L, mission.id)

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

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
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

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(1L, mission.id)

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

    Given("특정 과제의 과제 제출물들이 존재하는 경우") {
        val mission = createMission(id = 1L, submittable = true)
        val assignment = createAssignment(missionId = mission.id, id = 1L)
        val assignments = listOf(assignment)
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

        every { missionRepository.getById(any()) } returns mission
        every { assignmentRepository.findAllByMissionId(mission.id) } returns assignments
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("전체 자동 채점을 요청하면") {
            Then("정상으로 동작한다") {
                shouldNotThrowAny {
                    judgmentService.judgeAll(mission.id)
                }
            }
        }
    }

    Given("특정 과제 제출물에 대한 예제 자동 채점 기록이 있는 경우") {
        val assignment = createAssignment(pullRequestUrl = PULL_REQUEST_URL)
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
            val actual = judgmentService.findExample(1L, 1L)

            Then("자동 채점 결과를 돌려준다") {
                actual.shouldNotBeNull()
                actual.status shouldBe SUCCEEDED
            }
        }
    }

    Given("특정 과제 제출물에 대한 예제 자동 채점 기록이 없는 경우") {
        val assignment = createAssignment(pullRequestUrl = PULL_REQUEST_URL)

        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns null

        When("예제 자동 채점 결과를 조회하면") {
            val actual = judgmentService.findExample(1L, 1L)

            Then("null을 돌려준다") {
                actual.shouldBeNull()
            }
        }
    }

    Given("특정 자동 채점의 특정 커밋에 대한 자동 채점 기록이 있는 경우") {
        val commit = createCommit()
        val record = createJudgmentRecord(commit, JudgmentResult(), completedDateTime = null)
        val judgment = createJudgment(records = listOf(record), id = 1L)

        every { judgmentRepository.getById(any()) } returns judgment

        When("해당 커밋의 자동 채점이 성공하면") {
            judgmentService.success(judgment.id, createSuccessJudgmentRequest(commit = commit.hash))

            Then("자동 채점 기록의 상태가 성공으로 변경된다") {
                record.commit shouldBe commit
                record.status shouldBe SUCCEEDED
            }
        }

        When("해당 커밋의 자동 채점이 실패하면") {
            judgmentService.fail(judgment.id, createFailJudgmentRequest(commit = commit.hash))

            Then("자동 채점 기록의 상태가 실패로 변경된다") {
                record.commit shouldBe commit
                record.status shouldBe FAILED
            }
        }

        When("해당 커밋의 자동 채점이 취소되면") {
            judgmentService.cancel(judgment.id, createCancelJudgmentRequest(commit = commit.hash))

            Then("자동 채점 기록의 상태가 취소로 변경된다") {
                record.commit shouldBe commit
                record.status shouldBe CANCELLED
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
