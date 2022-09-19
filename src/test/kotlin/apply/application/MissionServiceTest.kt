package apply.application

import apply.createAssignment
import apply.createEvaluation
import apply.createJudgmentItem
import apply.createMission
import apply.createMissionData
import apply.createMissionResponse
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.mission.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import support.test.spec.afterRootTest
import java.util.Optional

class MissionServiceTest : BehaviorSpec({
    val missionRepository = mockk<MissionRepository>()
    val evaluationRepository = mockk<EvaluationRepository>()
    val evaluationTargetRepository = mockk<EvaluationTargetRepository>()
    val evaluationItemRepository = mockk<EvaluationItemRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val judgmentItemRepository = mockk<JudgmentItemRepository>()

    val missionService = MissionService(
        missionRepository,
        evaluationRepository,
        evaluationTargetRepository,
        evaluationItemRepository,
        assignmentRepository,
        judgmentItemRepository
    )

    Given("과제가 없는 평가가 있는 경우") {
        every { evaluationRepository.existsById(any()) } returns true
        every { missionRepository.existsByEvaluationId(any()) } returns false
        every { judgmentItemRepository.save(any()) } returns createJudgmentItem()
        every { missionRepository.save(any()) } returns createMission()

        When("해당 평가에 대한 과제를 생성하면") {
            val actual = missionService.save(createMissionData())

            Then("과제가 생성된다") {
                actual shouldBe createMissionResponse()
            }
        }
    }

    Given("평가가 존재하지 않는 경우") {
        every { evaluationRepository.existsById(any()) } returns false

        When("해당 평가에 대한 과제를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    missionService.save(createMissionData())
                }
            }
        }
    }

    Given("평가에 대한 과제가 이미 있는 경우") {
        every { evaluationRepository.existsById(any()) } returns true
        every { missionRepository.existsByEvaluationId(any()) } returns true

        When("해당 평가에 대한 과제를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    missionService.save(createMissionData())
                }
            }
        }
    }

    Given("특정 모집에 대한 평가 및 과제가 있는 경우") {
        val recruitmentId = 1L
        val evaluation1 = createEvaluation(id = 1L, title = "평가1", recruitmentId = recruitmentId)
        val evaluation2 = createEvaluation(id = 2L, title = "평가2", recruitmentId = recruitmentId)
        val mission1 = createMission(title = "과제1", evaluationId = 1L)
        val mission2 = createMission(title = "과제1", evaluationId = 2L)

        every { evaluationRepository.findAllByRecruitmentId(any()) } returns listOf(evaluation1, evaluation2)
        every { missionRepository.findAllByEvaluationIdIn(any()) } returns listOf(mission1, mission2)

        When("평가와 함께 해당 모집에 대한 모든 과제를 조회하면") {
            val actual = missionService.findAllByRecruitmentId(recruitmentId)

            Then("해당 모집에 대한 모든 과제와 관련된 평가를 확인할 수 있다") {
                actual shouldContainExactlyInAnyOrder listOf(
                    MissionAndEvaluationResponse(mission1, evaluation1),
                    MissionAndEvaluationResponse(mission2, evaluation2)
                )
            }
        }
    }

    Given("과제 및 평가 대상자가 있는 평가가 있고 해당 평가 대상자가 제출한 과제 제출물이 있는 경우") {
        val mission1 = createMission(id = 1L, hidden = false)
        val mission2 = createMission(id = 2L, hidden = false)
        val recruitmentId = 1L
        val userId = 1L

        every { evaluationRepository.findAllByRecruitmentId(any()) } returns listOf(
            createEvaluation(recruitmentId = recruitmentId, id = 1L),
            createEvaluation(recruitmentId = recruitmentId, id = 2L)
        )
        every { evaluationTargetRepository.existsByUserIdAndEvaluationId(any(), any()) } returns true
        every { missionRepository.findAllByEvaluationIdIn(any()) } returns listOf(mission1, mission2)
        every { assignmentRepository.findAllByUserId(any()) } returns listOf(
            createAssignment(id = userId, missionId = mission1.id)
        )

        When("해당 사용자의 특정 모집에 대한 모든 과제를 조회하면") {
            val actual = missionService.findAllByUserIdAndRecruitmentId(userId, recruitmentId)

            Then("과제 및 과제 제출물이 제출되었는지 확인할 수 있다") {
                actual shouldContainExactlyInAnyOrder listOf(
                    MyMissionResponse(mission1, true),
                    MyMissionResponse(mission2, false)
                )
            }
        }
    }

    Given("과제 제출물을 제출할 수 없는 과제가 있는 경우") {
        val mission = createMission(submittable = false)

        every { missionRepository.findByIdOrNull(mission.id) } returns mission
        every { missionRepository.deleteById(any()) } just Runs

        When("해당 과제를 삭제하면") {
            Then("과제를 삭제할 수 있다") {
                shouldNotThrowAny {
                    missionService.deleteById(mission.id)
                }
            }
        }
    }

    Given("과제 제출물을 제출할 수 있는 과제가 있는 경우") {
        val mission = createMission(submittable = true)

        every { missionRepository.findByIdOrNull(mission.id) } returns mission
        every { missionRepository.deleteById(any()) } just Runs

        When("해당 과제를 삭제하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    missionService.deleteById(mission.id)
                }
            }
        }
    }

    Given("과제가 존재하지 않는 경우") {
        every { missionRepository.findByIdOrNull(any()) } returns null
        every { missionRepository.deleteById(any()) } just Runs

        When("해당 과제를 삭제하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    missionService.deleteById(1L)
                }
            }
        }
    }

    Given("과제에 등록된 자동채점 평가항목이 존재하지 않는 경우") {
        val mission = createMission(title = "과제", evaluationId = 1L)
        val evaluation = createEvaluation(id = 1L, title = "평가1", recruitmentId = 1L)
        val judgmentItem = createJudgmentItem(id = 1L, missionId = 1L, evaluationItemId = null)

        every { missionRepository.getById(any()) } returns mission
        every { evaluationRepository.getById(any()) } returns evaluation
        every { judgmentItemRepository.findByMissionId(any()) } returns judgmentItem
        every { evaluationItemRepository.findById(any()) } returns Optional.ofNullable(null)

        When("자동채점 평가항목을 조회하면") {
            val actual = missionService.getDataById(judgmentItem.id)
                .evaluationItem

            Then("과제와 관련된 자동채점 평가항목을 확인할 수 없다") {
                actual?.id shouldBe 0L
                actual?.title shouldBe "평가 항목 없음"
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
