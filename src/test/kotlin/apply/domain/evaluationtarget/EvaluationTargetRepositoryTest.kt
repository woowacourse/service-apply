package apply.domain.evaluationtarget

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import java.util.stream.Stream

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
class EvaluationTargetRepositoryTest(
    private val evaluationTargetRepository: EvaluationTargetRepository
) {
    companion object {
        private const val EVALUATION_ID = 1L

        @JvmStatic
        fun provideExistsByEvaluationIdTestArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(1L, true),
                Arguments.of(2L, false)
            )
    }

    private val evaluationTargets: List<EvaluationTarget> = listOf(
        EvaluationTarget(
            EVALUATION_ID,
            1L
        ),
        EvaluationTarget(
            EVALUATION_ID,
            2L
        )
    )

    @BeforeEach
    internal fun setUp() {
        evaluationTargetRepository.saveAll(evaluationTargets)
    }

    @Test
    fun `평가의 id로 평가 대상자를 찾는다`() {
        val results = evaluationTargetRepository.findByEvaluationId(EVALUATION_ID)

        assertThat(results).usingElementComparatorOnFields("applicantId").isEqualTo(evaluationTargets)
    }

    @MethodSource("provideExistsByEvaluationIdTestArguments")
    @ParameterizedTest
    fun `평가의 id를 가지고 있는 평가 대상자의 존재 여부를 확인한다`(evaluationId: Long, expected: Boolean) {
        val actual = evaluationTargetRepository.existsByEvaluationId(evaluationId)

        assertThat(actual).isEqualTo(expected)
    }
}
