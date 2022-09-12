package apply.utils

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CsvGeneratorTest : StringSpec({
    "CSV 파일의 내용을 생성한다" {
        val headers = arrayOf("header1", "header2", "header3")
        val csvRows = listOf(
            CsvRow("csvContent1", "csvContent2", "csvContent3"),
            CsvRow("csvContent4", "csvContent5", "csvContent6"),
            CsvRow("csvContent7", "csvContent8", "csvContent9")
        )
        val actual = CsvGenerator().generateBy(headers, csvRows)
        actual.reader().readLines() shouldBe listOf(
            "header1,header2,header3",
            "csvContent1,csvContent2,csvContent3",
            "csvContent4,csvContent5,csvContent6",
            "csvContent7,csvContent8,csvContent9"
        )
    }
})
