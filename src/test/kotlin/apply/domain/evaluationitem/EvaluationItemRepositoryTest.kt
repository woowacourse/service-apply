package apply.domain.evaluationitem

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
internal class EvaluationItemRepositoryTest(
    private val evaluationItemRepository: EvaluationItemRepository
) {
    companion object {
        private const val EVALUATION_ID = 1L
        private const val MAXIMUM_SCORE = 3
    }

    @Test
    fun `평가의 id로 평가 항목들을 Position의 오름차순으로 조회한다`() {
        val evauationItems = listOf(
            EvaluationItem(
                title = "평가 항목 제목",
                description = "평가 항목 설명",
                evaluationId = EVALUATION_ID,
                maximumScore = MAXIMUM_SCORE,
                position = 2
            ),
            EvaluationItem(
                title = "평가 항목 제목2",
                description = "평가 항목 설명2",
                evaluationId = EVALUATION_ID,
                maximumScore = MAXIMUM_SCORE,
                position = 1
            ),
            EvaluationItem(
                title = "평가 항목 제목3",
                description = "평가 항목 설명3",
                evaluationId = EVALUATION_ID,
                maximumScore = MAXIMUM_SCORE,
                position = 3
            ),
            EvaluationItem(
                title = "평가 항목 제목4",
                description = "평가 항목 설명4",
                evaluationId = EVALUATION_ID,
                maximumScore = MAXIMUM_SCORE,
                position = 0
            )
        )

        evaluationItemRepository.saveAll(evauationItems)
        val results = evaluationItemRepository.findByEvaluationIdOrderByPosition(EVALUATION_ID)

        val expected = listOf(evauationItems[3], evauationItems[1], evauationItems[0], evauationItems[2])
        assertAll(
            { assertThat(results).usingElementComparatorIgnoringFields("id").isEqualTo(expected) },
            { assertThat(results).usingElementComparatorOnFields("id").isNotNull() }
        )
    }
}
