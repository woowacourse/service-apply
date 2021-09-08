package apply.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.BufferedReader

class CsvGeneratorTest {
    @Test
    fun `Csv 파일의 내용을 생성한다`() {
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
        assertThat(BufferedReader(actual.reader()).readLines()).isEqualTo(expected)
    }
}
