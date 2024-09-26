package apply.application

import apply.domain.assignment.AssignmentRepository
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
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
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val missionRepository: MissionRepository,
    private val assignmentRepository: AssignmentRepository,
    private val excelGenerator: ExcelGenerator
) {
    fun createApplicantExcel(recruitmentId: Long): ByteArrayInputStream {
        val applicants = applicantService.findAllByRecruitmentIdAndKeyword(recruitmentId)
        val titles = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId)
            .map { it.title }
            .toTypedArray()
        val headerTitles = arrayOf("이름", "이메일", "휴대전화 번호", "생년월일", "지원 일시", "부정행위자", "포트폴리오 URL", *titles)
        val excelRows = applicants.map {
            ExcelRow(
                it.name,
                it.email,
                it.phoneNumber,
                it.birthday.toString(),
                it.applicationForm.submittedDateTime.toString(),
                it.isCheater.toText(),
                it.applicationForm.referenceUrl,
                *it.applicationForm.answers.items.map { item -> item.contents }.toTypedArray(),
            )
        }
        return excelGenerator.generateBy(headerTitles, excelRows)
    }

    fun createTargetExcel(evaluationId: Long): ByteArrayInputStream {
        val targets = evaluationTargetService.findAllByEvaluationIdAndKeyword(evaluationId)
        val titles = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluationId)
            .map { it.title }
            .toTypedArray()
        val mission = missionRepository.findByEvaluationId(evaluationId)
        return mission?.let { createTargetExcelWithAssignment(titles, targets, mission) }
            ?: createTargetExcelDefault(titles, targets)
    }

    private fun createTargetExcelWithAssignment(
        titles: Array<String>,
        targets: List<EvaluationTargetResponse>,
        mission: Mission
    ): ByteArrayInputStream {
        val headerTitles = arrayOf(
            NAME, EMAIL, PULL_REQUEST_URL, ASSIGNMENT_NOTE, TOTAL_SCORE, STATUS, *titles, NOTE
        )
        val assignments = assignmentRepository.findAllByMissionId(mission.id)
        val excelRows = targets.map {
            val assignment = assignments.find { assignment -> assignment.memberId == it.memberId }
            ExcelRow(
                it.name,
                it.email,
                assignment?.url ?: UNSUBMITTED,
                assignment?.note ?: UNSUBMITTED,
                it.totalScore.toString(),
                it.evaluationStatus.toText(),
                *it.answers.map { item -> item.score.toString() }.toTypedArray(),
                it.note
            )
        }
        return excelGenerator.generateBy(headerTitles, excelRows)
    }

    private fun createTargetExcelDefault(
        titles: Array<String>,
        targets: List<EvaluationTargetResponse>
    ): ByteArrayInputStream {
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

    private fun Boolean.toText(): String {
        return when (this) {
            true -> "O"
            else -> "X"
        }
    }

    private fun EvaluationStatus.toText(): String {
        return when (this) {
            EvaluationStatus.WAITING -> "평가 전"
            EvaluationStatus.PASS -> "합격"
            EvaluationStatus.FAIL -> "탈락"
            EvaluationStatus.PENDING -> "보류"
        }
    }

    companion object {
        private const val NAME: String = "이름"
        private const val EMAIL: String = "이메일"
        private const val STATUS: String = "평가 상태"
        private const val NOTE: String = "기타 특이사항"
        private const val TOTAL_SCORE: String = "합계"
        private const val PULL_REQUEST_URL: String = "Pull Request URL"
        private const val ASSIGNMENT_NOTE: String = "소감"
        private const val UNSUBMITTED: String = "(미제출)"
    }
}
