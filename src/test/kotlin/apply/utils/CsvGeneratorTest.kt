package apply.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.ByteArrayInputStream

class CsvGeneratorTest {

    @Test
    fun `Csv 파일의 내용을 생성한다`() {
        //given
        val headers: Array<String> = arrayOf("header1", "header2", "header3")
        val csvRow: List<CsvRow> =
            listOf(
                CsvRow("csvContent1", "csvContent2", "csvContent3"),
                CsvRow("csvContent4", "csvContent5", "csvContent6"),
                CsvRow("csvContent7", "csvContent8", "csvContent9")
            )
        val csvGenerator = CsvGenerator()

        //then
        val generateBy: ByteArrayInputStream = csvGenerator.generateBy(headers, csvRow)
        val bufferedReader = BufferedReader(generateBy.reader())
        assertThat(bufferedReader.readLine()).isEqualTo("header1,header2,header3")
    }
}
