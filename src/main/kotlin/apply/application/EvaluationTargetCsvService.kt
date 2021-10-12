package apply.application

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
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
    private val assignmentService: AssignmentService,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val csvGenerator: CsvGenerator
) {
    fun createTargetCsv(evaluationId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val evaluationItemHeaders = evaluationItems.map { it.toHeader() }.toTypedArray()

        val headerTitles = arrayOf(ID, NAME, EMAIL, STATUS, *evaluationItemHeaders, NOTE)
        val csvRows = targets.map { target ->
            CsvRow(
                target.id.toString(),
                target.name,
                target.email,
                target.evaluationStatus.name,
                *scores(target.answers, evaluationItems).toTypedArray(),
                target.note
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }

    fun createTargetCsvWithAssignment(evaluationId: Long, missionId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val evaluationItemHeaders = evaluationItems.map { it.toHeader() }.toTypedArray()

        val headerTitles = arrayOf(
            ID,
            NAME,
            EMAIL,
            GITHUB_USERNAME,
            PULL_REQUEST_URL,
            ASSIGNMENT_NOTE,
            STATUS,
            *evaluationItemHeaders,
            NOTE
        )
        val assignments = assignmentService.findAllByEvaluationId(evaluationId)
        val csvRows = targets.map { target ->
            val assignment = assignments.find { assignment -> assignment.userId == target.userId }
            CsvRow(
                target.id.toString(),
                target.name,
                target.email,
                assignment?.githubUsername ?: UNSUBMITTED,
                assignment?.pullRequestUrl ?: UNSUBMITTED,
                assignment?.note ?: UNSUBMITTED,
                target.evaluationStatus.name,
                *scores(target.answers, evaluationItems).toTypedArray(),
                target.note
            )
        }
        return csvGenerator.generateBy(headerTitles, csvRows)
    }

    private fun scores(
        answers: List<EvaluationAnswerResponse>,
        evaluationItems: List<EvaluationItem>
    ): List<String> {
        val answerScores = answers.associate { it.evaluationItemId to it.score.toString() }
        return evaluationItems.map { answerScores[it.id] ?: "0" }
    }

    fun updateTarget(inputStream: InputStream, evaluationId: Long) {
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val evaluationTargetsData = getEvaluationTargetsDataFromCsv(inputStream, evaluationItems)
        evaluationTargetService.gradeAll(evaluationId, evaluationTargetsData)
    }

    private fun getEvaluationTargetsDataFromCsv(
        inputStream: InputStream,
        evaluationItems: List<EvaluationItem>
    ): Map<Long, EvaluationTargetData> {
        inputStream.bufferedReader().use { reader ->
            val csvParser = CSVParser(
                reader,
                CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim()
            )
            return csvParser.associate { csvRecord ->
                csvRecord.get(ID).toLong() to csvRecord.getEvaluationTargetData(evaluationItems)
            }
        }
    }

    private fun CSVRecord.getEvaluationStatus(): EvaluationStatus = EvaluationStatus.valueOf(get(STATUS).uppercase())

    private fun CSVRecord.getEvaluationAnswers(evaluationItems: List<EvaluationItem>): List<EvaluationItemScoreData> {
        return evaluationItems.map {
            val score = get(it.toHeader())?.toIntOrNull()
                ?: throw IllegalArgumentException("평가 항목의 점수에 숫자가 아닌 값이 들어갈 수 없습니다.")
            require(score <= it.maximumScore) { "평가 항목의 최대 점수보다 높은 점수입니다." }
            EvaluationItemScoreData(score, it.id)
        }
    }

    private fun CSVRecord.getEvaluationTargetData(evaluationItems: List<EvaluationItem>): EvaluationTargetData {
        val evaluationScores = getEvaluationAnswers(evaluationItems)
        val evaluationStatus = getEvaluationStatus()
        val note = get(NOTE)
        return EvaluationTargetData(evaluationScores, note, evaluationStatus)
    }

    private fun EvaluationItem.toHeader(): String = "$title($maximumScore)"

    companion object {
        private const val ID: String = "id"
        private const val NAME: String = "이름"
        private const val EMAIL: String = "이메일"
        private const val STATUS: String = "평가 상태"
        private const val NOTE: String = "기타 특이사항"
        private const val GITHUB_USERNAME: String = "Github Username"
        private const val PULL_REQUEST_URL: String = "Pull Request URL"
        private const val ASSIGNMENT_NOTE: String = "소감"
        private const val UNSUBMITTED: String = "(미제출)"
    }
}
