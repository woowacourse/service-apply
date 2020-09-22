package apply.application

import apply.utils.ApplicantExcelRow
import apply.utils.ExcelGenerator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayInputStream

@Service
@Transactional
class DownloadService(
    private val applicantService: ApplicantService
) {
    fun createExcelBy(recruitmentId: Long): ByteArrayInputStream {
        // Todo: recuritmentId 외 필요한 id를 모두 받아와 처리하도록 수정
        val applicantIds = listOf(1L, 2L, 3L)
        val applicants = applicantService.findAllByIds(applicantIds)
        val headerTitles = arrayOf("이름", "이메일", "전화번호", "성별", "생년월일")
        val excelRows = applicants.map {
            ApplicantExcelRow(
                applicantName = it.name,
                applicantEmail = it.email,
                applicantPhoneNumber = it.phoneNumber,
                applicantGender = it.gender.title,
                applicantBirthDay = it.birthday.toString()
            )
        }
        return ExcelGenerator.generateBy(headerTitles, excelRows)
    }
}
