package apply.application

import apply.COMMIT_HASH
import apply.PULL_REQUEST_URL
import apply.createAssignment
import apply.createCommit
import apply.createJudgment
import apply.createJudgmentFailRequest
import apply.createJudgmentRecord
import apply.createJudgmentResult
import apply.createJudgmentSuccessRequest
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.AssignmentArchive
import apply.domain.judgment.JudgmentRepository
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatus
import apply.domain.judgment.getById
import apply.domain.mission.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest
import java.time.LocalDateTime

class JudgmentServiceTest : BehaviorSpec({
    val judgmentRepository = mockk<JudgmentRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val missionRepository = mockk<MissionRepository>()
    val judgmentItemRepository = mockk<JudgmentItemRepository>()
    val assignmentArchive = mockk<AssignmentArchive>()

    val judgmentService = JudgmentService(
        judgmentRepository, assignmentRepository, missionRepository, judgmentItemRepository, assignmentArchive
    )

    Given("과제 제출 시간이 아닌 경우") {
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusMinutes(2), endDateTime = LocalDateTime.now().minusMinutes(1)
        )
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }

        When("해당 과제 제출물의 본 테스트를 실행하면") {
            val judgment = createJudgment()
            every { missionRepository.getById(any()) } returns createMission()
            every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
            every { judgmentItemRepository.existsByMissionId(any()) } returns true
            every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("정상적으로 채점을 진행한다") {
                val actual = judgmentService.judgeReal(1L, 1L)
                val expected = LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.STARTED,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )

                actual shouldBe expected
            }
        }
    }

    Given("자동 채점 항목이 존재하지 않는 경우") {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns false

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }

        When("해당 과제 제출물의 본 테스트를 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeReal(1L, 1L)
                }
            }
        }
    }

    Given("테스트 이력이 존재하고 제출한 commit id와 최신 이력의 commit id가 같은 경우") {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit()

        When("예제 테스트를 실행하면") {
            val judgment = createJudgment(records = listOf(createJudgmentRecord(result = createJudgmentResult())))
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("기존의 최신 테스트 실행 결과를 반환한다") {
                val response = judgmentService.judgeExample(1L, 1L)
                response shouldBe LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.SUCCEEDED,
                    5,
                    10,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )
            }
        }

        // todo: 추후 정책 수정될 수도 있음
        When("본 테스트를 실행하면") {
            val judgment = createJudgment(records = listOf(createJudgmentRecord(result = createJudgmentResult())))
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("기존의 최신 테스트 실행 결과를 반환한다") {
                val response = judgmentService.judgeReal(1L, 1L)
                response shouldBe LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.SUCCEEDED,
                    5,
                    10,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )
            }
        }
    }

    Given("테스트 이력이 존재하고 제출한 commit id와 최신 이력의 commit id가 다른 경우") {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit("other-commit-hash")

        When("예제 테스트를 실행하면") {
            val judgment = createJudgment(records = listOf(createJudgmentRecord(result = createJudgmentResult())))
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val response = judgmentService.judgeExample(1L, 1L)
                response shouldNotBe LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.STARTED,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )
            }
        }

        When("본 테스트를 실행하면") {
            val judgment = createJudgment(records = listOf(createJudgmentRecord(result = createJudgmentResult())))
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val response = judgmentService.judgeReal(1L, 1L)
                response shouldNotBe LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.STARTED,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )
            }
        }
    }

    Given("테스트 실행 이력이 존재하지 않는 경우") {
        every { missionRepository.getById(any()) } returns createMission()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentItemRepository.existsByMissionId(any()) } returns true
        every { assignmentArchive.getLastCommit(any(), any()) } returns createCommit("other-commit-hash")

        When("예제 테스트를 실행하면") {
            val judgment = createJudgment(records = listOf(createJudgmentRecord(result = createJudgmentResult())))
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val response = judgmentService.judgeExample(1L, 1L)
                response shouldNotBe LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.STARTED,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )
            }
        }

        When("본 테스트를 실행하면") {
            val judgment = createJudgment(records = listOf(createJudgmentRecord(result = createJudgmentResult())))
            every { judgmentRepository.findByAssignmentIdAndType(any(), any()) } returns judgment
            every { judgmentRepository.save(any()) } returns judgment

            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val response = judgmentService.judgeReal(1L, 1L)
                response shouldNotBe LastJudgmentResponse(
                    PULL_REQUEST_URL,
                    COMMIT_HASH,
                    JudgmentStatus.STARTED,
                    startedDateTime = judgment.lastRecord.startedDateTime
                )
            }
        }
    }

    Given("이전에 자동 채점을 실행한 경우") {
        When("자동 채점 응답 결과가 성공이면") {
            val record = createJudgmentRecord(result = JudgmentResult(), completedDateTime = null)
            every { judgmentRepository.getById(any()) } returns createJudgment(records = listOf(record))

            Then("자동 채점 성공 결과를 저장한다") {
                judgmentService.success(1L, createJudgmentSuccessRequest())

                record.status shouldBe JudgmentStatus.SUCCEEDED
            }
        }

        When("자동 채점 응답 결과가 실패면") {
            val record = createJudgmentRecord(result = JudgmentResult(), completedDateTime = null)
            every { judgmentRepository.getById(any()) } returns createJudgment(records = listOf(record))

            Then("자동 채점 실패 결과를 저장한다") {
                judgmentService.fail(1L, createJudgmentFailRequest())

                record.status shouldBe JudgmentStatus.FAILED
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
