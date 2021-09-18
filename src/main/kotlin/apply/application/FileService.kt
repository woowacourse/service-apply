package apply.application

import apply.domain.evaluationItem.EvaluationItem
import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.utils.CsvGenerator
import apply.utils.CsvRow
import apply.utils.ExcelGenerator
import apply.utils.ExcelRow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

@Transactional
@Service
class FileService(
    private val applicantService: ApplicantService,
    private val evaluationTargetService: EvaluationTargetService,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val excelGenerator: ExcelGenerator,
    private val csvGenerator: CsvGenerator
) {
    fun createApplicantExcel(recruitmentId: Long): ByteArrayInputStream {
        val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId)
        val titles =
            recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId).map { it.title }.toTypedArray()
        val headerTitles = arrayOf("이름", "이메일", "전화번호", "성별", "생년월일", "지원 일시", "부정 행위자", "포트폴리오 URL", *titles)
        val excelRows = applicants.map {
            ExcelRow(
                it.name,
                it.email,
                it.phoneNumber,
                it.gender.title,
                it.birthday.toString(),
                it.applicationForm.submittedDateTime.toString(),
                it.isCheater.toText(),
                it.applicationForm.referenceUrl,
                *it.applicationForm.answers.items.map { item -> item.contents }.toTypedArray()
            )
        }
        return excelGenerator.generateBy(headerTitles, excelRows)
    }

    fun createTargetExcel(evaluationId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val titles =
            evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId).map { it.title }.toTypedArray()
        val headerTitles = arrayOf("이름", "이메일", "합계", "평가상태", *titles, "기타 특이사항")
        val excelRows = targets.map {
            ExcelRow(
                it.name,
                it.email,
                it.totalScore.toString(),
                it.evaluationStatus.toText(),
                *it.answers.map { item -> item.score.toString() }.toTypedArray(),
                it.note
            )
        }
        return excelGenerator.generateBy(headerTitles, excelRows)
    }

    private fun Boolean.toText(): String {
        return when (this) {
            true -> "O"
            else -> "X"
        }
    }

    private fun EvaluationStatus.toText() =
        when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }

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

    private fun scores(
        answers: List<EvaluationAnswerResponse>,
        evaluationItems: List<EvaluationItem>
    ): List<String> {
        val answerScores = answers.associate { it.evaluationItemId to it.score.toString() }
        return evaluationItems.map { answerScores[it.id] ?: "0" }
    }

    companion object {
        const val ID: String = "id"
        const val NAME: String = "이름"
        const val EMAIL: String = "이메일"
        const val STATUS: String = "평가 상태"
        fun EvaluationItem.toHeader() = "${this.title}(${this.maximumScore})"
    }
}
