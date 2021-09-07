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
    private val applicantService: ApplicantService,
    private val evaluationTargetService: EvaluationTargetService,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val csvGenerator: CsvGenerator
) {
    fun createTargetCsv(evaluationId: Long): ByteArrayInputStream {
        val targets: List<EvaluationTargetResponse> =
            evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val evaluationItems: List<EvaluationItem> =
            evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val titles: Array<String> = evaluationItems.map { it.title + "(" + it.maximumScore + ")" }.toTypedArray()
        val scores = LinkedHashMap<Long, String>()

        val headerTitles = arrayOf("applicantId", "이름", "이메일", "평가 상태", *titles)
        val csvRows = targets.map {
            evaluationItems.associateTo(scores) { it.id to "0" }
            it.answers.map { item ->
                scores.put(item.evaluationItemId, item.score.toString())
            }
            val values = scores.values.toTypedArray()
            CsvRow(
                applicantService.getByEmail(it.email).id.toString(),
                it.name,
                it.email,
                it.evaluationStatus.name,
                *values
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }
}
