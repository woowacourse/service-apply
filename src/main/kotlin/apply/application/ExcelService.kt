package apply.application

import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.utils.ExcelGenerator
import apply.utils.ExcelRow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

@Service
@Transactional
class ExcelService(
    val applicantService: ApplicantService,
    val evaluationTargetService: EvaluationTargetService,
    val recruitmentItemRepository: RecruitmentItemRepository,
    val evaluationItemRepository: EvaluationItemRepository,
    val excelGenerator: ExcelGenerator
) {
    fun createApplicantExcel(recruitmentId: Long): ByteArrayInputStream {
        val applicants = applicantService.findAllByRecruitmentId(recruitmentId)
        val titles =
            recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId).map { it.title }.toTypedArray()
        val headerTitles = arrayOf("이름", "이메일", "전화번호", "성별", "생년월일", "지원 일시", "부정 행위자", *titles)
        val excelRows = applicants.map {
            ExcelRow(
                it.name,
                it.email,
                it.phoneNumber,
                it.gender.title,
                it.birthday.toString(),
                it.applicationForm.submittedDateTime.toString(),
                it.isCheater.toText(),
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
}
