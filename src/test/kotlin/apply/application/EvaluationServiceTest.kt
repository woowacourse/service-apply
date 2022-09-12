package apply.application

import apply.createEvaluation
import apply.createRecruitment
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.RecruitmentRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import support.test.spec.afterRootTest

class EvaluationServiceTest : BehaviorSpec({
    val recruitmentRepository = mockk<RecruitmentRepository>()
    val evaluationRepository = mockk<EvaluationRepository>()
    val evaluationItemRepository = mockk<EvaluationItemRepository>()

    val evaluationService = EvaluationService(evaluationRepository, evaluationItemRepository, recruitmentRepository)

    Given("다수의 모집과 모집에 대한 평가가 있는 경우") {
        val recruitment1 = createRecruitment(title = "모집1", id = 1L)
        val recruitment2 = createRecruitment(title = "모집2", id = 2L)
        val evaluation1 = createEvaluation(title = "평가1", recruitmentId = 1L, beforeEvaluationId = 0L, id = 1L)
        val evaluation2 = createEvaluation(title = "평가2", recruitmentId = 1L, beforeEvaluationId = 1L, id = 2L)
        val evaluation3 = createEvaluation(title = "평가3", recruitmentId = 2L, beforeEvaluationId = 0L, id = 3L)

        every { evaluationRepository.findAll() } returns listOf(evaluation1, evaluation2, evaluation3)
        every { recruitmentRepository.getOne(1L) } returns recruitment1
        every { recruitmentRepository.getOne(2L) } returns recruitment2
        every { evaluationRepository.getById(1L) } returns evaluation1
        every { evaluationRepository.getById(2L) } returns evaluation2

        When("모든 평가를 모집과 함께 조회하면") {
            val actual = evaluationService.findAllWithRecruitment()

            Then("모집명과 평가명을 확인할 수 있다") {
                actual[0] shouldBe EvaluationResponse(evaluation1, "모집1", 1L, "이전 평가 없음", 0L)
                actual[1] shouldBe EvaluationResponse(evaluation2, "모집1", 1L, "평가1", 1L)
                actual[2] shouldBe EvaluationResponse(evaluation3, "모집2", 2L, "이전 평가 없음", 0L)
            }
        }
    }

    Given("특정 평가가 이전 평가인 다른 평가가 있는 경우") {
        val evaluation1 = createEvaluation(id = 1L)
        val evaluation2 = createEvaluation(beforeEvaluationId = 1L, id = 2L)
        val evaluation3 = createEvaluation(beforeEvaluationId = 1L, id = 3L)

        every { evaluationRepository.deleteById(any()) } just Runs
        every { evaluationRepository.findAll() } returns listOf(evaluation1, evaluation2, evaluation3)

        When("특정 평가를 삭제하면") {
            evaluationService.deleteById(evaluation1.id)

            Then("이전 평가가 없도록 다른 평가가 재설정된다") {
                evaluation2.hasBeforeEvaluation().shouldBeFalse()
                evaluation3.hasBeforeEvaluation().shouldBeFalse()
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
