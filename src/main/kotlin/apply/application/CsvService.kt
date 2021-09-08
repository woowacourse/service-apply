package apply.application

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.utils.CsvGenerator
import apply.utils.CsvRow
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import javax.transaction.Transactional

@Transactional
@Service
class CsvService(
    private val evaluationTargetService: EvaluationTargetService,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val csvGenerator: CsvGenerator
) {
    fun createTargetCsv(evaluationId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val titles = evaluationItems.map { "${it.title}(${it.maximumScore})" }.toTypedArray()

        val headerTitles = arrayOf("id", "이름", "이메일", "평가 상태", *titles)
        val csvRows = targets.map { target ->
            CsvRow(
                target.id.toString(),
                target.name,
                target.email,
                target.evaluationStatus.name,
                *scores(target.answers, evaluationItems).values.toTypedArray()
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }

    private fun scores(
        answers: List<EvaluationAnswerResponse>,
        evaluationItems: List<EvaluationItem>
    ): Map<Long, String> {
        return evaluationItems.associate { it.id to "0" } +
            answers.associate { it.evaluationItemId to it.score.toString() }
    }
}
