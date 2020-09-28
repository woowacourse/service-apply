package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitment.Recruitment
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
import support.createLocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
internal class EvaluationServiceTest {
    @Mock
    private lateinit var recruitmentService: RecruitmentService

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
        evaluationService = EvaluationService(evaluationRepository, evaluationItemRepository, recruitmentService)

        recruitments = listOf(
            Recruitment(
                "웹 백엔드 2기",
                startDateTime = createLocalDateTime(2019, 10, 25, 10),
                endDateTime = createLocalDateTime(2019, 11, 5, 10),
                canRecruit = false
            ),
            Recruitment(
                "웹 백엔드 3기",
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10),
                canRecruit = true
            )
        )

        preCourseEvaluation = Evaluation(
            "프리코스 대상자 선발",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L,
            beforeEvaluationId = 0L,
            id = 1L
        )

        firstEvaluation = Evaluation(
            " 1주차 - 숫자야구게임",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L,
            beforeEvaluationId = 1L,
            id = 2L
        )

        secondEvaluation = Evaluation(
            "2주차 - 자동차경주게임 ",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L,
            beforeEvaluationId = 2L,
            id = 3L
        )

        evaluations = listOf(preCourseEvaluation, firstEvaluation, secondEvaluation)
    }

    @Test
    fun `평가와 모집 정보를 함께 제공한다`() {
        given(evaluationRepository.findAll()).willReturn(evaluations)
        given(recruitmentService.getById(anyLong())).willReturn(recruitments[0])
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
        val thirdEvaluation = Evaluation(
            "3주차 - 포커구현하기 ",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            recruitmentId = 1L,
            beforeEvaluationId = 2L,
            id = 4L
        )

        val evaluationsWithDuplicatedBeforeEvaluationId: List<Evaluation> =
            listOf(*evaluations.toTypedArray(), thirdEvaluation)

        given(evaluationRepository.findAll()).willReturn(evaluationsWithDuplicatedBeforeEvaluationId)
        willDoNothing().given(evaluationRepository).deleteById(anyLong())

        evaluationService.deleteById(2L)

        assertThat(0L).isEqualTo(evaluationsWithDuplicatedBeforeEvaluationId[2].beforeEvaluationId)
        assertThat(0L).isEqualTo(evaluationsWithDuplicatedBeforeEvaluationId[3].beforeEvaluationId)
    }
}
