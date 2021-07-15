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
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import support.test.UnitTest
import java.util.Optional

@UnitTest
internal class EvaluationServiceTest {
    @MockK
    private lateinit var recruitmentRepository: RecruitmentRepository

    @MockK
    private lateinit var evaluationRepository: EvaluationRepository

    @MockK
    private lateinit var evaluationItemRepository: EvaluationItemRepository

    private lateinit var evaluationService: EvaluationService
    private lateinit var recruitments: List<Recruitment>
    private lateinit var preCourseEvaluation: Evaluation
    private lateinit var firstEvaluation: Evaluation
    private lateinit var secondEvaluation: Evaluation
    private lateinit var evaluations: List<Evaluation>

    @BeforeEach
    internal fun setUp() {
        evaluationService = EvaluationService(evaluationRepository, evaluationItemRepository, recruitmentRepository)

        recruitments = listOf(
            createRecruitment(recruitable = false),
            createRecruitment(recruitable = true)
        )

        preCourseEvaluation = createEvaluation(title = EVALUATION_TITLE1, beforeEvaluationId = 0L, id = 1L)

        firstEvaluation = createEvaluation(title = EVALUATION_TITLE2, beforeEvaluationId = 1L, id = 2L)

        secondEvaluation = createEvaluation(title = EVALUATION_TITLE3, beforeEvaluationId = 2L, id = 3L)

        evaluations = listOf(preCourseEvaluation, firstEvaluation, secondEvaluation)
    }

    @Test
    fun `평가와 모집 정보를 함께 제공한다`() {
        every { evaluationRepository.findAll() } returns evaluations
        every { recruitmentRepository.getOne(any()) } returns recruitments[0]
        every { evaluationRepository.findById(1L) } returns Optional.of(evaluations[0])
        every { evaluationRepository.findById(2L) } returns Optional.of(evaluations[1])

        val findAllWithRecruitment = evaluationService.findAllWithRecruitment()

        assertAll(
            { assertThat(2L).isEqualTo(findAllWithRecruitment[1].id) },
            { assertThat(firstEvaluation.title).isEqualTo(findAllWithRecruitment[1].title) },
            { assertThat(firstEvaluation.description).isEqualTo(findAllWithRecruitment[1].description) },
            { assertThat(recruitments[0].title).isEqualTo(findAllWithRecruitment[1].recruitmentTitle) },
            { assertThat(firstEvaluation.recruitmentId).isEqualTo(findAllWithRecruitment[1].recruitmentId) },
            { assertThat(preCourseEvaluation.title).isEqualTo(findAllWithRecruitment[1].beforeEvaluationTitle) },
            { assertThat(preCourseEvaluation.id).isEqualTo(findAllWithRecruitment[1].beforeEvaluationId) }
        )
    }

    @Test
    fun `삭제된 평가를 이전 평가로 가지는 평가의 이전 평가를 초기화한다`() {
        every { evaluationRepository.findAll() } returns evaluations
        every { evaluationRepository.deleteById(any()) } just Runs

        evaluationService.deleteById(2L)

        assertThat(0L).isEqualTo(evaluations[2].beforeEvaluationId)
    }

    @Test
    fun `삭제된 평가를 이전 평가로 가지는 평가들의 이전 평가를 초기화한다`() {
        val thirdEvaluation = createEvaluation(beforeEvaluationId = 2L, id = 4L)

        val evaluationsWithDuplicatedBeforeEvaluationId: List<Evaluation> =
            listOf(*evaluations.toTypedArray(), thirdEvaluation)

        every { evaluationRepository.findAll() } returns evaluationsWithDuplicatedBeforeEvaluationId
        every { evaluationRepository.deleteById(any()) } just Runs

        evaluationService.deleteById(2L)

        assertThat(0L).isEqualTo(evaluationsWithDuplicatedBeforeEvaluationId[2].beforeEvaluationId)
        assertThat(0L).isEqualTo(evaluationsWithDuplicatedBeforeEvaluationId[3].beforeEvaluationId)
    }
}
