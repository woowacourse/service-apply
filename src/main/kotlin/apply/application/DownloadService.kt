package apply.application

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

        return ExcelGenerator.generateBy(applicants)
    }
}
