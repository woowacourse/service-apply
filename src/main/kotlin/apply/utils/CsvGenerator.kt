package apply.utils

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class CsvGenerator {
    fun generateBy(headerTitles: Array<String>, rows: List<CsvRow>): ByteArrayInputStream {
        val csvPrinter = CSVPrinter(StringBuilder(), CSVFormat.DEFAULT).apply {
            printRecords(headerTitles)
            rows.forEach { printRecord(it.data) }
        }
        return ByteArrayInputStream(csvPrinter.out.toString().toByteArray())
    }
}
