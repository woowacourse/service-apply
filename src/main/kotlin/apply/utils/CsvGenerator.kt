package apply.utils

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class CsvGenerator {
    fun generateBy(headerTitles: Array<String>, rows: List<CsvRow>): ByteArrayInputStream {
        return CSVPrinter(StringBuilder(), CSVFormat.DEFAULT.withHeader(*headerTitles)).use { csvPrinter ->
            rows.forEach { csvPrinter.printRecord(it.data) }
            ByteArrayInputStream(csvPrinter.out.toString().toByteArray())
        }
    }
}
