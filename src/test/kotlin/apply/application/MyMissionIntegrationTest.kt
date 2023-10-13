package apply.application

import apply.EMAIL
import apply.createAssignment
import apply.createEvaluation
import apply.createEvaluationTarget
import apply.createJudgmentItem
import apply.createMission
import apply.createRecruitment
import apply.createUser
import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.judgmentitem.JudgmentItemRepository
import apply.domain.mission.MissionRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.user.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import org.springframework.transaction.annotation.Transactional
import support.test.IntegrationTest

@Transactional
@IntegrationTest
class MyMissionIntegrationTest(
    private val myMissionService: MyMissionService,
    private val userRepository: UserRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val missionRepository: MissionRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val assignmentRepository: AssignmentRepository
) : BehaviorSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    fun saveEvaluationTarget(evaluationId: Long, email: String = EMAIL): EvaluationTarget {
        val user = userRepository.save(createUser(email = email))
        return evaluationTargetRepository.save(createEvaluationTarget(evaluationId = evaluationId, userId = user.id))
    }

    Given("특정 모집의 과제가 있는 평가에 여러 평가 대상자가 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment())
        val evaluation = evaluationRepository.save(createEvaluation(recruitmentId = recruitment.id))
        missionRepository.save(createMission(evaluationId = evaluation.id, hidden = false))
        saveEvaluationTarget(evaluation.id, "jason@email.com")
        val target = saveEvaluationTarget(evaluation.id, "pepper@email.com")

        When("해당 모집에 대한 특정 지원자의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByUserIdAndRecruitmentId(target.userId, recruitment.id)

            Then("모든 과제를 확인할 수 있다") {
                actual shouldHaveSize 1
            }
        }
    }

    Given("특정 모집의 평가에 자동 채점 항목이 없는 과제 및 평가 대상자가 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment())
        val evaluation = evaluationRepository.save(createEvaluation(recruitmentId = recruitment.id))
        missionRepository.save(createMission(evaluationId = evaluation.id, hidden = false))
        val target = saveEvaluationTarget(evaluation.id)

        When("해당 모집에 대한 지원자의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByUserIdAndRecruitmentId(target.userId, recruitment.id)

            Then("해당 과제에 대한 과제 제출물을 제출하지 않았으며 예제 테스트를 실행할 수 없음을 알 수 있다") {
                actual shouldHaveSize 1
                actual[0].submitted.shouldBeFalse()
                actual[0].testable.shouldBeFalse()
            }
        }
    }

    Given("특정 모집의 평가에 자동 채점 항목이 있는 과제 및 평가 대상자가 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment())
        val evaluation = evaluationRepository.save(createEvaluation(recruitmentId = recruitment.id))
        val mission = missionRepository.save(createMission(evaluationId = evaluation.id, hidden = false))
        judgmentItemRepository.save(createJudgmentItem(missionId = mission.id))
        val target = saveEvaluationTarget(evaluation.id)

        When("해당 모집에 대한 지원자의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByUserIdAndRecruitmentId(target.userId, recruitment.id)

            Then("해당 과제에 대한 과제 제출물을 제출하지 않았으며 예제 테스트를 실행할 수 있음을 알 수 있다") {
                actual shouldHaveSize 1
                actual[0].submitted.shouldBeFalse()
                actual[0].testable.shouldBeTrue()
            }
        }
    }

    Given("특정 모집의 평가에 자동 채점 항목이 없는 과제 및 과제 제출물을 제출한 평가 대상자가 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment())
        val evaluation = evaluationRepository.save(createEvaluation(recruitmentId = recruitment.id))
        val mission = missionRepository.save(createMission(evaluationId = evaluation.id, hidden = false))
        val target = saveEvaluationTarget(evaluation.id)
        assignmentRepository.save(createAssignment(target.userId, mission.id))

        When("해당 모집에 대한 지원자의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByUserIdAndRecruitmentId(target.userId, recruitment.id)

            Then("해당 과제에 대한 과제 제출물을 제출했으며 예제 테스트를 실행할 수 없음을 알 수 있다") {
                actual shouldHaveSize 1
                actual[0].submitted.shouldBeTrue()
                actual[0].testable.shouldBeFalse()
            }
        }
    }

    Given("특정 모집의 평가에 자동 채점 항목이 있는 과제 및 과제 제출물을 제출한 평가 대상자가 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment())
        val evaluation = evaluationRepository.save(createEvaluation(recruitmentId = recruitment.id))
        val mission = missionRepository.save(createMission(evaluationId = evaluation.id, hidden = false))
        judgmentItemRepository.save(createJudgmentItem(missionId = mission.id))
        val target = saveEvaluationTarget(evaluation.id)
        assignmentRepository.save(createAssignment(target.userId, mission.id))

        When("해당 모집에 대한 지원자의 모든 과제를 조회하면") {
            val actual = myMissionService.findAllByUserIdAndRecruitmentId(target.userId, recruitment.id)

            Then("해당 과제에 대한 과제 제출물을 제출했으며 예제 테스트를 실행할 수 있음을 알 수 있다") {
                actual shouldHaveSize 1
                actual[0].submitted.shouldBeTrue()
                actual[0].testable.shouldBeTrue()
            }
        }
    }
})
