package apply.application

import apply.domain.evaluationItem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.utils.ExcelGenerator
import apply.utils.ExcelRow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

@Transactional
@Service
class ExcelService(
    private val applicantService: ApplicantService,
    private val evaluationTargetService: EvaluationTargetService,
    private val assignmentService: AssignmentService,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val excelGenerator: ExcelGenerator
) {
    fun createApplicantExcel(recruitmentId: Long): ByteArrayInputStream {
        val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId)
        val titles = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId)
            .map { it.title }
            .toTypedArray()
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
        val headerTitles = arrayOf(NAME, EMAIL, TOTAL_SCORE, STATUS, *titles, NOTE)
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

    fun createTargetExcelWithAssignment(evaluationId: Long, missionId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val assignments = assignmentService.findAllByEvaluationId(evaluationId)
        val titles =
            evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId).map { it.title }.toTypedArray()
        val headerTitles =
            arrayOf(NAME, EMAIL, GITHUB_USERNAME, PULL_REQUEST_URL, ASSIGNMENT_NOTE, TOTAL_SCORE, STATUS, *titles, NOTE)
        val excelRows = targets.map {
            val assignment = assignments.find { each -> each.userId == it.userId }
            ExcelRow(
                it.name,
                it.email,
                assignment?.githubUsername ?: UNSUBMITTED,
                assignment?.pullRequestUrl ?: UNSUBMITTED,
                assignment?.note ?: UNSUBMITTED,
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

    companion object {
        private const val NAME: String = "이름"
        private const val EMAIL: String = "이메일"
        private const val STATUS: String = "평가 상태"
        private const val NOTE: String = "기타 특이사항"
        private const val TOTAL_SCORE: String = "합계"
        private const val GITHUB_USERNAME: String = "Github 유저 네임"
        private const val PULL_REQUEST_URL: String = "Pull Request URL"
        private const val ASSIGNMENT_NOTE: String = "소감"
        private const val UNSUBMITTED: String = "(미제출)"
    }
}
