package apply.application

import apply.domain.evaluationItem.EvaluationItemRepository
import apply.utils.CsvGenerator
import apply.utils.CsvRow
import java.io.ByteArrayInputStream

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
        val headerTitles = arrayOf("applicantId", "이름", "이메일", *titles, "평가 상태")

        val csvRows = targets.map {
            CsvRow(
                applicantService.getByEmail(it.email).id.toString(),
                it.name,
                it.email,
                *it.answers.map { item -> item.score.toString() }.toTypedArray(),
                it.evaluationStatus.name
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }
}
