package apply.application

import apply.domain.recruitmentitem.RecruitmentItemRepository
import apply.utils.ExcelGenerator
import apply.utils.ExcelRow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

@Service
@Transactional
class ExcelService(
    val recruitmentItemRepository: RecruitmentItemRepository
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

    fun Boolean.toText(): String {
        return when (this) {
            true -> "O"
            else -> "X"
        }
    }
}
