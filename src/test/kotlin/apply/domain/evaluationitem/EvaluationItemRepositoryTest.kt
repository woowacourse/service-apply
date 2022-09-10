package apply.domain.evaluationitem

import apply.createEvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeSortedBy
import support.test.RepositoryTest

@RepositoryTest
class EvaluationItemRepositoryTest(
    private val evaluationItemRepository: EvaluationItemRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("평가 조회") {
        val evaluationId = 1L
        evaluationItemRepository.saveAll(
            listOf(
                createEvaluationItem(evaluationId = evaluationId, position = 2),
                createEvaluationItem(evaluationId = evaluationId, position = 1),
                createEvaluationItem(evaluationId = evaluationId, position = 3),
                createEvaluationItem(evaluationId = evaluationId, position = 0)
            )
        )

        expect("특정 평가의 평가 항목을 순서대로 조회한다") {
            val actual = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
            actual.shouldBeSortedBy { it.position }
        }
    }
})
