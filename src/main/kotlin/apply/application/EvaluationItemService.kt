package apply.application

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Transactional
@Service
class EvaluationItemService(private val evaluationItemRepository: EvaluationItemRepository) {
    fun findByEvaluationId(evaluationId: Long): List<EvaluationItem> {
        return evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
    }

    @PostConstruct
    private fun populateDummy() {
        if (evaluationItemRepository.count() != 0L) {
            return
        }
        val evaluationItems = listOf(
            EvaluationItem(
                title = "README.md 파일에 기능 목록이 추가되어 있는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 1L,
                maximumScore = 2,
                position = 0
            ),
            EvaluationItem(
                title = "인덴트가 2 이하인가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 1L,
                maximumScore = 3,
                position = 2
            ),
            EvaluationItem(
                title = "하드코딩을 상수화 했는가?",
                description = "[리뷰 절차]\n" +
                    "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse",
                evaluationId = 1L,
                maximumScore = 2,
                position = 1
            )
        )
        evaluationItemRepository.saveAll(evaluationItems)
    }
}
