package apply.application

import apply.createEvaluation
import apply.createMission
import apply.createMissionData
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getOrThrow
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import support.test.spec.afterRootTest

class MissionServiceTest : BehaviorSpec({
    val missionRepository = mockk<MissionRepository>()
    val evaluationRepository = mockk<EvaluationRepository>()
    val evaluationItemRepository = mockk<EvaluationItemRepository>()
    val judgmentItemRepository = mockk<JudgmentItemRepository>()

    val missionService = MissionService(
        missionRepository,
        evaluationRepository,
        evaluationItemRepository,
        judgmentItemRepository
    )

    Given("과제가 없는 평가가 있는 경우") {
        val evaluation = createEvaluation()

        every { evaluationRepository.getOrThrow(any()) } returns evaluation
        every { missionRepository.findByIdOrNull(any()) } returns null
        every { missionRepository.existsByEvaluationId(any()) } returns false
        every { missionRepository.save(any()) } returns createMission(evaluationId = evaluation.id)
        every { judgmentItemRepository.findByMissionId(any()) } returns null

        When("해당 평가에 대한 과제를 생성하면") {
            Then("과제가 생성된다") {
                shouldNotThrowAny {
                    missionService.save(createMissionData(evaluation = EvaluationSelectData(evaluation)))
                }
            }
        }
    }

    Given("평가가 존재하지 않는 경우") {
        every { evaluationRepository.getOrThrow(any()) } throws NoSuchElementException()

        When("해당 평가에 대한 과제를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    missionService.save(createMissionData())
                }
            }
        }
    }

    Given("평가에 대한 과제가 이미 있는 경우") {
        val evaluation = createEvaluation()

        every { evaluationRepository.getOrThrow(any()) } returns evaluation
        every { missionRepository.findByIdOrNull(any()) } returns null
        every { missionRepository.existsByEvaluationId(any()) } returns true

        When("해당 평가에 대한 과제를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    missionService.save(createMissionData(evaluation = EvaluationSelectData(evaluation)))
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

    afterRootTest {
        clearAllMocks()
    }
})
