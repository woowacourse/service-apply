package apply.application

import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
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
    private lateinit var firstWeekEvaluation: Evaluation
    private lateinit var secondWeekEvaluation: Evaluation
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

        firstWeekEvaluation = Evaluation(
            " 1주차 - 숫자야구게임",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            1L,
            1L,
            2L
        )

        secondWeekEvaluation = Evaluation(
            "2주차 - 자동차경주게임 ",
            "[리뷰 절차]\n" +
                "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
            1L,
            2L,
            3L
        )

        evaluations = listOf(preCourseEvaluation, firstWeekEvaluation, secondWeekEvaluation)
    }

    @Test
    fun `평가와 모집 정보를 함께 제공한다`() {
        `when`(evaluationRepository.findAll()).thenReturn(evaluations)
        `when`(recruitmentRepository.findById(anyLong())).thenReturn(Optional.of(recruitments[0]))
        `when`(evaluationRepository.findById(1L)).thenReturn(Optional.of(evaluations[0]))
        `when`(evaluationRepository.findById(2L)).thenReturn(Optional.of(evaluations[1]))

        val findAllWithRecruitment = evaluationService.findAllWithRecruitment()

        assertAll(
            { assertEquals(findAllWithRecruitment[1].id, 2L) },
            { assertEquals(findAllWithRecruitment[1].title, firstWeekEvaluation.title) },
            { assertEquals(findAllWithRecruitment[1].description, firstWeekEvaluation.description) },
            { assertEquals(findAllWithRecruitment[1].recruitment, recruitments[0].title) },
            { assertEquals(findAllWithRecruitment[1].recruitmentId, firstWeekEvaluation.recruitment) },
            { assertEquals(findAllWithRecruitment[1].beforeEvaluation, preCourseEvaluation.title) },
            { assertEquals(findAllWithRecruitment[1].beforeEvaluationId, preCourseEvaluation.id) }
        )
    }

    @Test
    fun `삭제된 평가를 이전 평가로 가지는 평가의 이전 평가를 초기화한다`() {
        `when`(evaluationRepository.findAll()).thenReturn(evaluations)
        doNothing().`when`(evaluationRepository).deleteById(anyLong())

        evaluationService.deleteById(2L)

        assertEquals(evaluations[2].beforeEvaluation, 0L)
    }
}
