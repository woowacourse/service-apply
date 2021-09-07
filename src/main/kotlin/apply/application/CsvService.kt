package apply.application

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
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val titles =
            evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
                .map { it.title + "(" + it.maximumScore + ")" }.toTypedArray()
        val headerTitles = arrayOf("applicantId", "이름", "이메일", "평가 상태", *titles)

        val csvRows = targets.map {
            CsvRow(
                applicantService.getByEmail(it.email).id.toString(),
                it.name,
                it.email,
                it.evaluationStatus.name,
                *it.answers.map { item -> item.score.toString() }.toTypedArray()
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }
}
