package apply.application

import apply.application.FileService.Companion.EMAIL
import apply.application.FileService.Companion.ID
import apply.application.FileService.Companion.NAME
import apply.application.FileService.Companion.STATUS
import apply.application.FileService.Companion.toHeader
import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationStatus
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.transaction.Transactional

@Transactional
@Service
class CsvTargetService(
    private val evaluationItemRepository: EvaluationItemRepository
) {
    fun updateTarget(reader: InputStreamReader, evaluationId: Long) {
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
        val csvParser = CSVParser(
            BufferedReader(reader),
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
            // TODO: 평가 대상자 상태 업데이트
            print("$targetId, $name, $email, $evaluationStatus")
        }
    }

    private fun readEvaluationAnswers(
        evaluationItems: List<EvaluationItem>,
        csvRecord: CSVRecord
    ) = evaluationItems.map { evaluationItem ->
        val score = csvRecord.get(evaluationItem.toHeader()).toInt()
        require(score <= evaluationItem.maximumScore) {
            "평가 항목의 최대 점수보다 높은 점수입니다."
        }
        EvaluationAnswer(score, evaluationItem.id)
    }
}
