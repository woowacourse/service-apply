package apply.application

import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.utils.ExcelGenerator
import apply.utils.ExcelRow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

@Service
@Transactional
class ExcelService(
    val recruitmentItemRepository: RecruitmentItemRepository,
    val evaluationItemRepository: EvaluationItemRepository
) {
    fun createApplicantExcel(applicants: List<ApplicantResponse>): ByteArrayInputStream {
        val recruitmentItemIds = applicants[0].applicationForm.answers.items.map { it.recruitmentItemId }
        val titles = recruitmentItemRepository.findAllById(recruitmentItemIds).map { it.title }.toTypedArray()
        val headerTitles = arrayOf("이름", "이메일", "전화번호", "성별", "생년월일", "지원 일시", "부정 행위자", *titles)
        val excelRows = applicants.map {
            ExcelRow(
                listOf(
                    it.name,
                    it.email,
                    it.phoneNumber,
                    it.gender.title,
                    it.birthday.toString(),
                    it.applicationForm.submittedDateTime.toString(),
                    it.isCheater.toText(),
                    *it.applicationForm.answers.items.map { item -> item.contents }.toTypedArray()
                )
            )
        }
        return ExcelGenerator.generateBy(headerTitles, excelRows)
    }

    fun createTargetExcel(targets: List<EvaluationTargetResponse>): ByteArrayInputStream {
        val evaluationItemIds = targets[0].target.evaluationAnswers.answers.map { it.evaluationItemId }
        val titles = evaluationItemRepository.findAllById(evaluationItemIds).map { it.title }.toTypedArray()
        val headerTitles = arrayOf("이름", "이메일", "합계", "평가상태", *titles, "기타 특이사항")
        val excelRows = targets.map {
            ExcelRow(
                listOf(
                    it.name,
                    it.email,
                    it.target.evaluationAnswers.answers.map { item -> item.score }.sum().toString(),
                    it.target.evaluationStatus.title,
                    *it.target.evaluationAnswers.answers.map { item -> item.score.toString() }.toTypedArray(),
                    it.target.note
                )
            )
        }
        return ExcelGenerator.generateBy(headerTitles, excelRows)
    }

    fun Boolean.toText(): String {
        return when (this) {
            true -> "O"
            else -> "X"
        }
    }
}
