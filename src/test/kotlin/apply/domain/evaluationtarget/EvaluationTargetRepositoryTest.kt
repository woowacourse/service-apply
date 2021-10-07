package apply.domain.evaluationtarget

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import support.test.RepositoryTest

@RepositoryTest
class EvaluationTargetRepositoryTest(
    private val evaluationTargetRepository: EvaluationTargetRepository
) {
    companion object {
        private const val EVALUATION_ID = 1L
    }

    private val evaluationTargets: List<EvaluationTarget> = listOf(
        EvaluationTarget(
            EVALUATION_ID,
            userId = 1L
        ),
        EvaluationTarget(
            EVALUATION_ID,
            userId = 2L
        )
    )

    @BeforeEach
    internal fun setUp() {
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    @Test
    fun `평가의 id로 평가 대상자를 찾는다`() {
        val results = evaluationTargetRepository.findAllByEvaluationId(EVALUATION_ID)

        assertThat(results).usingElementComparatorOnFields("userId").isEqualTo(evaluationTargets)
    }

    @CsvSource(value = ["1,true", "2,false"])
    @ParameterizedTest
    fun `평가의 id를 가지고 있는 평가 대상자의 존재 여부를 확인한다`(evaluationId: Long, expected: Boolean) {
        val actual = evaluationTargetRepository.existsByEvaluationId(evaluationId)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `지원자의 id들에 해당되는 평가 대상자를 제거한다`() {
        evaluationTargetRepository.deleteByUserIdIn(setOf(1L, 2L))

        assertThat(evaluationTargetRepository.count()).isEqualTo(0)
    }

    @Test
    fun `지정한 평가에 해당되고 지원자의 id들에 해당되는 평가 대상자를 제거한다`() {
        evaluationTargetRepository.save(
            EvaluationTarget(
                evaluationId = 2L,
                userId = 1L
            )
        )

        evaluationTargetRepository.deleteByEvaluationIdAndUserIdIn(1L, setOf(1L, 2L))

        assertThat(evaluationTargetRepository.count()).isEqualTo(1)
    }

    @ParameterizedTest
    @CsvSource(value = ["PASS,2", "FAIL,1"])
    fun `지정한 평가에 해당되고 평가대상자들의 평가 상태가 특정 평가 상태와 일치하는 평가 대상자를 찾는다`(
        evaluationStatus: EvaluationStatus,
        expectedSize: Int
    ) {
        evaluationTargetRepository.saveAll(
            listOf(
                EvaluationTarget(EVALUATION_ID, userId = 1L, evaluationStatus = EvaluationStatus.PASS),
                EvaluationTarget(EVALUATION_ID, userId = 2L, evaluationStatus = EvaluationStatus.PASS),
                EvaluationTarget(EVALUATION_ID, userId = 3L, evaluationStatus = EvaluationStatus.FAIL)
            )
        )
        val actual =
            evaluationTargetRepository.findAllByEvaluationIdAndEvaluationStatus(EVALUATION_ID, evaluationStatus)
        assertThat(actual).hasSize(expectedSize)
    }
}
