package apply.application

import apply.createAssignment
import apply.createEvaluation
import apply.createEvaluationTarget
import apply.createJudgmentItem
import apply.createMission
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.judgmentitem.JudgmentItem
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest

@Transactional
@IntegrationTest
class MyMissionIntegrationTest(
    private val myMissionService: MyMissionService,
    private val evaluationRepository: EvaluationRepository,
    private val missionRepository: MissionRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository,
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    fun saveMission(
        recruitmentId: Long = 1L,
        submittable: Boolean = true,
        hidden: Boolean = false,
    ): Mission {
        val evaluation = evaluationRepository.save(createEvaluation(recruitmentId = recruitmentId))
        return missionRepository.save(
            createMission(
                evaluationId = evaluation.id,
                submittable = submittable,
                hidden = hidden
            )
        )
    }

    fun saveEvaluationTarget(evaluationId: Long, memberId: Long): EvaluationTarget {
        return evaluationTargetRepository.save(createEvaluationTarget(evaluationId = evaluationId, memberId = memberId))
    }

    fun saveJudgmentItem(missionId: Long): JudgmentItem {
        return judgmentItemRepository.save(createJudgmentItem(missionId = missionId))
    }

    fun saveAssignment(memberId: Long, missionId: Long): Assignment {
        return assignmentRepository.save(createAssignment(memberId = memberId, missionId = missionId))
    }

    Given("특정 모집의 과제가 있는 여러 평가와 평가 대상자가 있는 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission1 = saveMission(recruitmentId)
        val mission2 = saveMission(recruitmentId)
        saveEvaluationTarget(evaluationId = mission1.evaluationId, memberId = memberId)
        saveEvaluationTarget(evaluationId = mission2.evaluationId, memberId = memberId)

        When("해당 모집에 대한 특정 회원의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByMemberIdAndRecruitmentId(memberId, recruitmentId)

            Then("모든 과제를 확인할 수 있다") {
                actual shouldHaveSize 2
                actual[0].shouldBe(mission1, submitted = false, testable = false)
                actual[1].shouldBe(mission2, submitted = false, testable = false)
            }
        }
    }

    Given("특정 회원이 과제가 있는 일부 평가에 대해서만 평가 대상자인 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission = saveMission(recruitmentId)
        saveMission(recruitmentId)
        saveEvaluationTarget(evaluationId = mission.evaluationId, memberId = memberId)

        When("해당 모집에 대한 특정 회원의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByMemberIdAndRecruitmentId(memberId, recruitmentId)

            Then("일부 과제만 확인할 수 있다") {
                actual shouldHaveSize 1
                actual[0].shouldBe(mission, submitted = false, testable = false)
            }
        }
    }

    Given("특정 회원이 평가 대상자이더라도 과제가 비공개인 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission1 = saveMission(recruitmentId, hidden = true)
        val mission2 = saveMission(recruitmentId)
        saveEvaluationTarget(evaluationId = mission1.evaluationId, memberId = memberId)
        saveEvaluationTarget(evaluationId = mission2.evaluationId, memberId = memberId)

        When("해당 모집에 대한 특정 회원의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByMemberIdAndRecruitmentId(memberId, recruitmentId)

            Then("일부 과제만 확인할 수 있다") {
                actual shouldHaveSize 1
                actual[0].shouldBe(mission2, submitted = false, testable = false)
            }
        }
    }

    Given("일부 과제에 대해 과제 제출물을 제출한 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission1 = saveMission(recruitmentId)
        val mission2 = saveMission(recruitmentId)
        saveEvaluationTarget(evaluationId = mission1.evaluationId, memberId = memberId)
        saveEvaluationTarget(evaluationId = mission2.evaluationId, memberId = memberId)
        saveAssignment(memberId = memberId, missionId = mission1.id)

        When("해당 모집에 대한 특정 회원의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByMemberIdAndRecruitmentId(memberId, recruitmentId)

            Then("과제 제출물이 제출되었는지 확인할 수 있다") {
                actual shouldHaveSize 2
                actual[0].shouldBe(mission1, submitted = true, testable = false)
                actual[1].shouldBe(mission2, submitted = false, testable = false)
            }
        }
    }

    Given("일부 과제에 자동 채점 항목이 있는 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission1 = saveMission(recruitmentId)
        val mission2 = saveMission(recruitmentId)
        saveEvaluationTarget(evaluationId = mission1.evaluationId, memberId = memberId)
        saveEvaluationTarget(evaluationId = mission2.evaluationId, memberId = memberId)
        saveJudgmentItem(missionId = mission1.id)

        When("해당 모집에 대한 특정 회원의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByMemberIdAndRecruitmentId(memberId, recruitmentId)

            Then("과제 제출물의 제출 여부와 관계없이 예제 테스트를 실행할 수 있다") {
                actual shouldHaveSize 2
                actual[0].shouldBe(mission1, submitted = false, testable = true)
                actual[1].shouldBe(mission2, submitted = false, testable = false)
            }
        }
    }

    Given("자동 채점 항목이 있는 과제에 대해 과제 제출물을 제출한 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission1 = saveMission(recruitmentId)
        val mission2 = saveMission(recruitmentId)
        saveEvaluationTarget(evaluationId = mission1.evaluationId, memberId = memberId)
        saveEvaluationTarget(evaluationId = mission2.evaluationId, memberId = memberId)
        saveJudgmentItem(missionId = mission1.id)
        saveAssignment(memberId = memberId, missionId = mission1.id)

        When("해당 모집에 대한 특정 회원의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByMemberIdAndRecruitmentId(memberId, recruitmentId)

            Then("과제 제출물이 제출되었는지 확인할 수 있으며 예제 테스트를 실행할 수 있다") {
                actual shouldHaveSize 2
                actual[0].shouldBe(mission1, submitted = true, testable = true)
                actual[1].shouldBe(mission2, submitted = false, testable = false)
            }
        }
    }

    Given("과제가 공개이고 과제 기간 내에 있는 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission = saveMission(recruitmentId, hidden = false)
        saveEvaluationTarget(evaluationId = mission.id, memberId = memberId)

        When("평가 대상자가 과제를 조회하면") {
            val actual = myMissionService.findByMemberIdAndMissionId(memberId, mission.id)

            Then("과제를 확인할 수 있다") {
                actual.shouldNotBeNull()
                actual.description.shouldNotBeNull()
            }
        }

        When("평가 대상자가 아닌 회원이 과제를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByMemberIdAndMissionId(2L, mission.id)
                }
            }
        }
    }

    Given("과제 기간 내이지만 과제가 비공개인 경우") {
        val memberId = 1L
        val recruitmentId = 1L
        val mission = saveMission(recruitmentId, hidden = true)
        saveEvaluationTarget(evaluationId = mission.id, memberId = memberId)

        When("평가 대상자가 과제를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByMemberIdAndMissionId(memberId, mission.id)
                }
            }
        }

        When("평가 대상자가 아닌 회원이 과제를 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<NoSuchElementException> {
                    myMissionService.findByMemberIdAndMissionId(2L, mission.id)
                }
            }
        }
    }
})

private fun MyMissionAndJudgementResponse.shouldBe(mission: Mission, submitted: Boolean, testable: Boolean) {
    this.id shouldBe mission.id
    this.submitted shouldBe submitted
    this.testable shouldBe testable
}
