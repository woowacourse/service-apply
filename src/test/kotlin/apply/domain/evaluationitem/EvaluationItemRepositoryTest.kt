package apply.domain.evaluationitem

import apply.EVALUATION_ID
import apply.createEvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import support.test.RepositoryTest

@RepositoryTest
internal class EvaluationItemRepositoryTest(
    private val evaluationItemRepository: EvaluationItemRepository
) {
    @Test
    fun `평가의 id로 평가 항목들을 Position의 오름차순으로 조회한다`() {
        val evaluationItems = listOf(
            createEvaluationItem(position = 2),
            createEvaluationItem(position = 1),
            createEvaluationItem(position = 3),
            createEvaluationItem(position = 0)
        )

        evaluationItemRepository.saveAll(evaluationItems)
        val results = evaluationItemRepository.findByEvaluationIdOrderByPosition(EVALUATION_ID)

        val expected = listOf(evaluationItems[3], evaluationItems[1], evaluationItems[0], evaluationItems[2])
        assertThat(results).isEqualTo(expected)
    }
}
