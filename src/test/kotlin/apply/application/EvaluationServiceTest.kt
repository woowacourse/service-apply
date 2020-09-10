package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import support.createLocalDateTime
import java.util.Optional

internal class EvaluationServiceTest {
    @Autowired
    private lateinit var evaluationService: EvaluationService
    private val recruitmentRepository: RecruitmentRepository = mock(RecruitmentRepository::class.java)
    private val evaluationRepository: EvaluationRepository = mock(EvaluationRepository::class.java)
    private lateinit var recruitments: List<Recruitment>
    private lateinit var preCourseEvaluation: Evaluation
    private lateinit var firstEvaluation: Evaluation
    private lateinit var secondEvaluation: Evaluation
    private lateinit var evaluations: List<Evaluation>

    @BeforeEach
    internal fun setUp() {
        evaluationService = EvaluationService(evaluationRepository, RecruitmentService(recruitmentRepository))

        recruitments = listOf(
            Recruitment(
                "웹 백엔드 2기",
                false,
                createLocalDateTime(2019, 10, 25, 10),
                createLocalDateTime(2019, 11, 5, 10)
            ),
            Recruitment(
                "웹 백엔드 3기",
                true,
                createLocalDateTime(2020, 10, 25, 15),
                createLocalDateTime(2020, 11, 5, 10)
            )
        )

        preCourseEvaluation = Evaluation(
            "프리코스 대상자 선발",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            1L,
            0L,
            1L
        )

        firstEvaluation = Evaluation(
            " 1주차 - 숫자야구게임",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            1L,
            1L,
            2L
        )

        secondEvaluation = Evaluation(
            "2주차 - 자동차경주게임 ",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            1L,
            2L,
            3L
        )

        evaluations = listOf(preCourseEvaluation, firstEvaluation, secondEvaluation)
    }

    @Test
    fun `평가와 모집 정보를 함께 제공한다`() {
        given(evaluationRepository.findAll()).willReturn(evaluations)
        given(recruitmentRepository.findById(anyLong())).willReturn(Optional.of(recruitments[0]))
        given(evaluationRepository.findById(1L)).willReturn(Optional.of(evaluations[0]))
        given(evaluationRepository.findById(2L)).willReturn(Optional.of(evaluations[1]))

        val findAllWithRecruitment = evaluationService.findAllWithRecruitment()

        assertAll(
            { assertThat(2L).isEqualTo(findAllWithRecruitment[1].id) },
            { assertThat(firstEvaluation.title).isEqualTo(findAllWithRecruitment[1].title) },
            { assertThat(firstEvaluation.description).isEqualTo(findAllWithRecruitment[1].description) },
            { assertThat(recruitments[0].title).isEqualTo(findAllWithRecruitment[1].recruitment) },
            { assertThat(firstEvaluation.recruitment).isEqualTo(findAllWithRecruitment[1].recruitmentId) },
            { assertThat(preCourseEvaluation.title).isEqualTo(findAllWithRecruitment[1].beforeEvaluation) },
            { assertThat(preCourseEvaluation.id).isEqualTo(findAllWithRecruitment[1].beforeEvaluationId) }
        )
    }

    @Test
    fun `삭제된 평가를 이전 평가로 가지는 평가의 이전 평가를 초기화한다`() {
        given(evaluationRepository.findAll()).willReturn(evaluations)
        willDoNothing().given(evaluationRepository).deleteById(anyLong())

        evaluationService.deleteById(2L)

        assertThat(0L).isEqualTo(evaluations[2].beforeEvaluation)
    }
}
