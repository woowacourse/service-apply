package apply.application

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationStatus
import apply.utils.CsvGenerator
import apply.utils.CsvRow
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import java.io.BufferedReader
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
        val titles = evaluationItems.map { titles(it) }.toTypedArray()

        val headerTitles = arrayOf(ID, NAME, EMAIL, STATUS, *titles)
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

    private fun titles(it: EvaluationItem) = "${it.title}(${it.maximumScore})"

    private fun scores(
        answers: List<EvaluationAnswerResponse>,
        evaluationItems: List<EvaluationItem>
    ): Map<Long, String> {
        return evaluationItems.associate { it.id to "0" } +
            answers.associate { it.evaluationItemId to it.score.toString() }
    }

    fun readTargetCsv(memoryBuffer: MemoryBuffer, evaluationId: Long) {
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val reader = BufferedReader(memoryBuffer.inputStream.reader())
        val csvParser = CSVParser(
            reader,
            CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withTrim()
        )
        for (csvRecord in csvParser) {
            var evaluationId = csvRecord.get(ID)
            var name = csvRecord.get(NAME)
            var email = csvRecord.get(EMAIL)
            var evaluationStatus = EvaluationStatus.valueOf(csvRecord.get(STATUS))
            val evaluationAnswers = evaluationItems.map { evaluationItem ->
                EvaluationAnswer(
                    csvRecord.get(titles(evaluationItem)).toInt(),
                    evaluationItem.id
                )
            }
        }
    }

    companion object {
        private const val ID: String = "id"
        private const val NAME: String = "이름"
        private const val EMAIL: String = "이메일"
        private const val STATUS: String = "평가 상태"
    }
}
