package apply.application

import apply.COMMIT_HASH
import apply.PULL_REQUEST_URL
import apply.createAssignment
import apply.createCancelJudgmentRequest
import apply.createCommit
import apply.createJudgment
import apply.createFailJudgmentRequest
import apply.createJudgmentRecord
import apply.createSuccessJudgmentRequest
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.getById
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
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

    Given("예제 자동 채점 시 과제 제출 시간이 아닌 경우") {
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = now().minusMinutes(2), endDateTime = now().minusMinutes(1)
        )

        When("해당 과제 제출물의 예제 자동 채점을 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }
    }

    Given("본 자동 채점 시 과제 제출 시간이 아닌 경우") {
        val judgment = createJudgment(type = JudgmentType.REAL)
        val missionId = 1L

        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = now().minusMinutes(2), endDateTime = now().minusMinutes(1)
        )
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { missionRepository.getById(any()) } returns createMission(id = missionId)
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { judgmentRepository.save(any()) } returns judgment

        When("해당 과제 제출물의 본 자동 채점를 실행하면") {
            val actual = judgmentService.judgeReal(1L, missionId)

            Then("정상적으로 채점을 진행한다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe JudgmentStatus.STARTED
                }
            }
        }
    }

    Given("자동 채점 항목이 존재하지 않는 경우") {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns false

        When("해당 과제 제출물의 예제 자동 채점을 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }

        When("해당 과제 제출물의 본 자동 채점을 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeReal(1L, 1L)
                }
            }
        }
    }

    Given("예제 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 같은 경우") {
        val judgment = createJudgment(
            type = JudgmentType.EXAMPLE,
            records = listOf(
                createJudgmentRecord(
                    result = JudgmentResult(9, 10, status = JudgmentStatus.SUCCEEDED),
                    completedDateTime = now()
                )
            )
        )

        every { missionRepository.getById(any()) } returns createMission(submittable = true)
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
        every { judgmentRepository.save(any()) } returns judgment

        When("예제 자동 채점을 실행하면") {
            val actual = judgmentService.judgeExample(1L, 1L)

            Then("이전의 자동 채점 결과를 반환한다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe JudgmentStatus.SUCCEEDED
                    passCount shouldBe 9
                    totalCount shouldBe 10
                }
            }
        }
    }

    Given("본 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 같은 경우") {
        val judgment = createJudgment(
            type = JudgmentType.REAL,
            records = listOf(
                createJudgmentRecord(
                    result = JudgmentResult(9, 10, status = JudgmentStatus.SUCCEEDED),
                    completedDateTime = now()
                )
            )
        )

        every { missionRepository.getById(any()) } returns createMission(submittable = true)
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
        every { judgmentRepository.save(any()) } returns judgment

        When("본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(1L, 1L)

            Then("이전의 자동 채점 결과를 반환한다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe JudgmentStatus.SUCCEEDED
                    passCount shouldBe 9
                    totalCount shouldBe 10
                }
            }
        }
    }

    Given("예제 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 다른 경우") {
        val assignment = createAssignment(pullRequestUrl = PULL_REQUEST_URL, id = 1L)
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = JudgmentType.EXAMPLE,
            records = listOf(
                createJudgmentRecord(
                    commit = createCommit("commit1"),
                    startedDateTime = now().minusMinutes(5)
                )
            )
        )
        val commit = createCommit("commit2")

        every { missionRepository.getById(any()) } returns createMission(submittable = true)
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("예제 자동 채점을 실행하면") {
            val actual = judgmentService.judgeExample(1L, 1L)

            Then("최신 커밋에 대한 자동 채점 결과를 확인할 수 있다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe commit.hash
                    status shouldBe JudgmentStatus.STARTED
                }
            }
        }
    }

    Given("본 자동 채점 기록이 존재하고 이전 커밋과 최신 커밋이 다른 경우") {
        val assignment = createAssignment(pullRequestUrl = PULL_REQUEST_URL, id = 1L)
        val judgment = createJudgment(
            assignmentId = assignment.id,
            type = JudgmentType.REAL,
            records = listOf(
                createJudgmentRecord(
                    commit = createCommit("commit1"),
                    startedDateTime = now().minusMinutes(5)
                )
            )
        )
        val commit = createCommit("commit2")

        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns commit
        every { judgmentRepository.save(any()) } returns judgment

        When("자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(1L, 1L)

            Then("최신 커밋에 대한 자동 채점 결과를 확인할 수 있다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe commit.hash
                    status shouldBe JudgmentStatus.STARTED
                }
            }
        }
    }

    Given("예제 자동 채점 기록이 존재하지 않는 경우") {
        val judgment = createJudgment(type = JudgmentType.EXAMPLE)

        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
        every { judgmentRepository.save(any()) } returns judgment

        When("예제 자동 채점을 실행하면") {
            val actual = judgmentService.judgeExample(1L, 1L)

            Then("해당 커밋에 대한 자동 채점 결과를 확인할 수 있다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe JudgmentStatus.STARTED
                }
            }
        }
    }

    Given("본 자동 채점 기록이 존재하지 않는 경우") {
        val judgment = createJudgment(type = JudgmentType.REAL)

        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
        every { judgmentRepository.save(any()) } returns judgment

        When("본 자동 채점을 실행하면") {
            val actual = judgmentService.judgeReal(1L, 1L)

            Then("해당 커밋에 대한 자동 채점 결과를 확인할 수 있다") {
                assertSoftly(actual) {
                    pullRequestUrl shouldBe PULL_REQUEST_URL
                    commitHash shouldBe COMMIT_HASH
                    status shouldBe JudgmentStatus.STARTED
                }
            }
        }
    }

    Given("이전에 자동 채점을 실행한 경우") {
        val record = createJudgmentRecord(result = JudgmentResult(), completedDateTime = null)
        every { judgmentRepository.getById(any()) } returns createJudgment(records = listOf(record))

        When("자동 채점 성공 응답 결과를 저장하면") {
            judgmentService.success(1L, createSuccessJudgmentRequest())

            Then("이전 채점 기록의 상태가 성공이 된다") {
                record.status shouldBe JudgmentStatus.SUCCEEDED
            }
        }

        When("자동 채점 실패 응답 결과를 저장하면") {
            judgmentService.fail(1L, createFailJudgmentRequest())

            Then("이전 채점 기록의 상태가 실패가 된다") {
                record.status shouldBe JudgmentStatus.FAILED
            }
        }

        When("자동 채점 취소 응답 결과를 저장하면") {
            judgmentService.cancel(1L, createCancelJudgmentRequest())

            Then("이전 채점 기록의 상태가 취소가 된다") {
                record.status shouldBe JudgmentStatus.CANCELLED
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
