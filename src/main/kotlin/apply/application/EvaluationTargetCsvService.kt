package apply.application

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationStatus
import apply.utils.CsvGenerator
import apply.utils.CsvRow
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.transaction.Transactional

@Transactional
@Service
class EvaluationTargetCsvService(
    private val evaluationTargetService: EvaluationTargetService,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val csvGenerator: CsvGenerator
) {
    fun createTargetCsv(evaluationId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val evaluationItemHeaders = evaluationItems.map { it.toHeader() }.toTypedArray()

        val headerTitles = arrayOf(ID, NAME, EMAIL, STATUS, *evaluationItemHeaders)
        val csvRows = targets.map { target ->
            CsvRow(
                target.id.toString(),
                target.name,
                target.email,
                target.evaluationStatus.name,
                *scores(target.answers, evaluationItems).toTypedArray()
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }

    private fun EvaluationItem.toHeader(): String = "${this.title}(${this.maximumScore})"

    private fun scores(
        answers: List<EvaluationAnswerResponse>,
        evaluationItems: List<EvaluationItem>
    ): List<String> {
        val answerScores = answers.associate { it.evaluationItemId to it.score.toString() }
        return evaluationItems.map { answerScores[it.id] ?: "0" }
    }

    fun updateTarget(inputStream: InputStream, evaluationId: Long) {
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        inputStream.bufferedReader().use { reader ->
            val csvParser = CSVParser(
                reader,
                CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim()
            )
            for (csvRecord in csvParser) {
                var targetId = csvRecord.get(ID)
                var name = csvRecord.get(NAME)
                var email = csvRecord.get(EMAIL)
                var evaluationStatus = EvaluationStatus.valueOf(csvRecord.get(STATUS))
                val evaluationAnswers = readEvaluationAnswers(evaluationItems, csvRecord)
                // TODO: 평가 대상자 상태 업데이트 기능 구현
            }
        }
    }

    private fun readEvaluationAnswers(
        evaluationItems: List<EvaluationItem>,
        csvRecord: CSVRecord
    ): List<EvaluationAnswer> = evaluationItems.map { evaluationItem ->
        val score = csvRecord.get(evaluationItem.toHeader())
        require(score.isNotBlank()) { "평가 항목의 점수에 빈 값이 들어갈 수 없습니다" }
        require(score.toInt() <= evaluationItem.maximumScore) { "평가 항목의 최대 점수보다 높은 점수입니다." }
        EvaluationAnswer(score.toInt(), evaluationItem.id)
    }

    companion object {
        private const val ID: String = "id"
        private const val NAME: String = "이름"
        private const val EMAIL: String = "이메일"
        private const val STATUS: String = "평가 상태"
    }
}
