package apply.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.ByteArrayInputStream

class CsvGeneratorTest {

    @Test
    fun `Csv 파일의 내용을 생성한다`() {
        //given
        val lineSeparator = System.lineSeparator()
        val headers: Array<String> = arrayOf("header1", "header2", "header3")
        val csvRow: List<CsvRow> =
            listOf(
                CsvRow("csvContent1", "csvContent2", "csvContent3"),
                CsvRow("csvContent4", "csvContent5", "csvContent6"),
                CsvRow("csvContent7", "csvContent8", "csvContent9")
            )
        val csvGenerator = CsvGenerator()
        val expected = "header1,header2,header3" + lineSeparator +
            "csvContent1,csvContent2,csvContent3" + lineSeparator +
            "csvContent4,csvContent5,csvContent6" + lineSeparator +
            "csvContent7,csvContent8,csvContent9"
        //when
        val generatedCsvContent: ByteArrayInputStream = csvGenerator.generateBy(headers, csvRow)
        //then
        val bufferedReader = BufferedReader(generatedCsvContent.reader())

        assertThat(expected)
            .isEqualTo(bufferedReader.readLines().joinToString(lineSeparator))
    }
}
