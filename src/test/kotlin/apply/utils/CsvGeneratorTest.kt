package apply.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.io.BufferedReader

class CsvGeneratorTest : DescribeSpec({
    describe("CsvGenerator") {
        it("Csv 파일의 내용을 생성한다") {
            // given
            val headers = arrayOf("header1", "header2", "header3")
            val csvRows = listOf(
                    CsvRow("csvContent1", "csvContent2", "csvContent3"),
                    CsvRow("csvContent4", "csvContent5", "csvContent6"),
                    CsvRow("csvContent7", "csvContent8", "csvContent9")
            )

            // when
            val actual = CsvGenerator().generateBy(headers, csvRows)

            // then
            val expected = listOf(
                    "header1,header2,header3",
                    "csvContent1,csvContent2,csvContent3",
                    "csvContent4,csvContent5,csvContent6",
                    "csvContent7,csvContent8,csvContent9"
            )
            BufferedReader(actual.reader()).readLines() shouldBe expected
        }
    }
})
