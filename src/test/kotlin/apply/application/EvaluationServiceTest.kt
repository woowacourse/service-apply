package apply.application

import apply.EVALUATION_TITLE1
import apply.EVALUATION_TITLE2
import apply.EVALUATION_TITLE3
import apply.createEvaluation
import apply.createRecruitment
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import support.test.UnitTest
import java.util.Optional

@UnitTest
internal class EvaluationServiceTest : DescribeSpec({
    @MockK
    val recruitmentRepository: RecruitmentRepository = mockk()

    @MockK
    val evaluationRepository: EvaluationRepository = mockk()

    @MockK
    val evaluationItemRepository: EvaluationItemRepository = mockk()

    val evaluationService: EvaluationService =
        EvaluationService(evaluationRepository, evaluationItemRepository, recruitmentRepository)
    val recruitments: List<Recruitment> = listOf(
        createRecruitment(recruitable = false),
        createRecruitment(recruitable = true)
    )
    val preCourseEvaluation: Evaluation = createEvaluation(title = EVALUATION_TITLE1, beforeEvaluationId = 0L, id = 1L)
    val firstEvaluation: Evaluation = createEvaluation(title = EVALUATION_TITLE2, beforeEvaluationId = 1L, id = 2L)
    val secondEvaluation: Evaluation = createEvaluation(title = EVALUATION_TITLE3, beforeEvaluationId = 2L, id = 3L)
    val evaluations: List<Evaluation> = listOf(preCourseEvaluation, firstEvaluation, secondEvaluation)

    describe("EvaluationService") {
        it("평가와 모집 정보를 함께 제공한다") {
            every { evaluationRepository.findAll() } returns evaluations
            every { recruitmentRepository.getOne(any()) } returns recruitments[0]
            every { evaluationRepository.findById(1L) } returns Optional.of(evaluations[0])
            every { evaluationRepository.findById(2L) } returns Optional.of(evaluations[1])

            val findAllWithRecruitment = evaluationService.findAllWithRecruitment()

            assertSoftly {
                findAllWithRecruitment[1].id shouldBe 2L
                firstEvaluation.title shouldBe findAllWithRecruitment[1].title
                firstEvaluation.description shouldBe findAllWithRecruitment[1].description
                recruitments[0].title shouldBe findAllWithRecruitment[1].recruitmentTitle
                firstEvaluation.recruitmentId shouldBe findAllWithRecruitment[1].recruitmentId
                preCourseEvaluation.title shouldBe findAllWithRecruitment[1].beforeEvaluationTitle
                preCourseEvaluation.id shouldBe findAllWithRecruitment[1].beforeEvaluationId
            }
        }

        it("삭제된 평가를 이전 평가로 가지는 평가의 이전 평가를 초기화한다") {
            every { evaluationRepository.findAll() } returns evaluations
            every { evaluationRepository.deleteById(any()) } just Runs

            evaluationService.deleteById(2L)

            evaluations[2].beforeEvaluationId shouldBe 0L
        }

        it("삭제된 평가를 이전 평가로 가지는 평가들의 이전 평가를 초기화한다") {
            val thirdEvaluation = createEvaluation(beforeEvaluationId = 2L, id = 4L)

            val evaluationsWithDuplicatedBeforeEvaluationId: List<Evaluation> =
                listOf(*evaluations.toTypedArray(), thirdEvaluation)

            every { evaluationRepository.findAll() } returns evaluationsWithDuplicatedBeforeEvaluationId
            every { evaluationRepository.deleteById(any()) } just Runs

            evaluationService.deleteById(2L)

            assertSoftly {
                evaluationsWithDuplicatedBeforeEvaluationId[2].beforeEvaluationId shouldBe 0L
                evaluationsWithDuplicatedBeforeEvaluationId[3].beforeEvaluationId shouldBe 0L
            }
        }
    }
})
