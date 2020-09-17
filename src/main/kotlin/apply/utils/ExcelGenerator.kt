package apply.utils

import apply.domain.applicant.Applicant
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ExcelGenerator {
    companion object {
        private const val HEADER_ROW = 0
        private const val DATA_START_ROW = 1
        private const val APPLICANT_NAME_COL = 0;
        private const val APPLICANT_EMAIL_COL = 1;
        private const val APPLICANT_PHONE_NUMBER_COL = 2;
        private const val APPLICANT_GENDER_COL = 3;
        private const val APPLICANT_BIRTHDAY_COL = 4;

        private val APPLICANT_HEADERS = arrayOf("이름", "이메일", "전화번호", "성별", "생년월일")

        fun generateBy(applicants: List<Applicant>): ByteArrayInputStream {
            // TODO: applicants 외 필요한 도메인 List를 받아오도록 수정
            val columns = APPLICANT_HEADERS
            val workbook = SXSSFWorkbook()
            val sheet = workbook.createSheet()

            styleHeaders(workbook, sheet, columns)
            fillApplicantsData(sheet, applicants, columns)

            val out = ByteArrayOutputStream()
            workbook.write(out)
            workbook.close()

            return ByteArrayInputStream(out.toByteArray())
        }

        private fun styleHeaders(workbook: SXSSFWorkbook, sheet: SXSSFSheet, columns: Array<String>) {
            val headerFont = workbook.createFont()
            headerFont.bold = true

            val headerCellStyle = workbook.createCellStyle()
            headerCellStyle.setFont(headerFont)
            headerCellStyle.fillBackgroundColor = IndexedColors.GREY_25_PERCENT.index

            val headerRow = sheet.createRow(HEADER_ROW)

            for (col in columns.indices) {
                val cell = headerRow.createCell(col)
                cell.setCellValue(columns[col])
                cell.cellStyle = headerCellStyle
            }
        }

        private fun fillApplicantsData(sheet: SXSSFSheet, applicants: List<Applicant>, columns: Array<String>) {
            var rowIndex = DATA_START_ROW

            sheet.trackAllColumnsForAutoSizing()
            for (applicant in applicants) {
                val row = sheet.createRow(rowIndex++)
                row.createCell(APPLICANT_NAME_COL).setCellValue(applicant.name)
                row.createCell(APPLICANT_EMAIL_COL).setCellValue(applicant.email)
                row.createCell(APPLICANT_PHONE_NUMBER_COL).setCellValue(applicant.phoneNumber)
                row.createCell(APPLICANT_GENDER_COL).setCellValue(applicant.gender.title)
                row.createCell(APPLICANT_BIRTHDAY_COL).setCellValue(applicant.birthday.toString())
            }

            for (col in columns.indices) {
                sheet.autoSizeColumn(col)
            }
        }
    }
}
