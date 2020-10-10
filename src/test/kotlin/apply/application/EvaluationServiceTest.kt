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
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
internal class EvaluationServiceTest {
    @Mock
    private lateinit var recruitmentRepository: RecruitmentRepository

    @Mock
    private lateinit var recruitmentItemRepository: RecruitmentItemRepository

    @Mock
    private lateinit var evaluationRepository: EvaluationRepository

    @Mock
    private lateinit var evaluationItemRepository: EvaluationItemRepository

    private lateinit var evaluationService: EvaluationService
    private lateinit var recruitments: List<Recruitment>
    private lateinit var preCourseEvaluation: Evaluation
    private lateinit var firstEvaluation: Evaluation
    private lateinit var secondEvaluation: Evaluation
    private lateinit var evaluations: List<Evaluation>

    @BeforeEach
    internal fun setUp() {
        evaluationService = EvaluationService(evaluationRepository, evaluationItemRepository, recruitmentRepository, recruitmentItemRepository)

        recruitments = listOf(
            createRecruitment(canRecruit = false),
            createRecruitment(canRecruit = true)
        )

        preCourseEvaluation = createEvaluation(title = EVALUATION_TITLE1, beforeEvaluationId = 0L, id = 1L)

        firstEvaluation = createEvaluation(title = EVALUATION_TITLE2, beforeEvaluationId = 1L, id = 2L)

        secondEvaluation = createEvaluation(title = EVALUATION_TITLE3, beforeEvaluationId = 2L, id = 3L)

        evaluations = listOf(preCourseEvaluation, firstEvaluation, secondEvaluation)
    }

    @Test
    fun `평가와 모집 정보를 함께 제공한다`() {
        given(evaluationRepository.findAll()).willReturn(evaluations)
        given(recruitmentRepository.getOne(anyLong())).willReturn(recruitments[0])
        given(evaluationRepository.findById(1L)).willReturn(Optional.of(evaluations[0]))
        given(evaluationRepository.findById(2L)).willReturn(Optional.of(evaluations[1]))

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
        given(evaluationRepository.findAll()).willReturn(evaluations)
        willDoNothing().given(evaluationRepository).deleteById(anyLong())

        evaluationService.deleteById(2L)

        assertThat(0L).isEqualTo(evaluations[2].beforeEvaluationId)
    }

    @Test
    fun `삭제된 평가를 이전 평가로 가지는 평가들의 이전 평가를 초기화한다`() {
        val thirdEvaluation = createEvaluation(beforeEvaluationId = 2L, id = 4L)

        val evaluationsWithDuplicatedBeforeEvaluationId: List<Evaluation> =
            listOf(*evaluations.toTypedArray(), thirdEvaluation)

        given(evaluationRepository.findAll()).willReturn(evaluationsWithDuplicatedBeforeEvaluationId)
        willDoNothing().given(evaluationRepository).deleteById(anyLong())

        evaluationService.deleteById(2L)

        assertThat(0L).isEqualTo(evaluationsWithDuplicatedBeforeEvaluationId[2].beforeEvaluationId)
        assertThat(0L).isEqualTo(evaluationsWithDuplicatedBeforeEvaluationId[3].beforeEvaluationId)
    }
}
