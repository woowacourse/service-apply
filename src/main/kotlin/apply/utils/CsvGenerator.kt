package apply.utils

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class CsvGenerator {
    fun generateBy(headerTitles: Array<String>, rows: List<CsvRow>): ByteArrayInputStream {

        val csvContent = StringBuilder()

        val csvPrinter = CSVPrinter(csvContent, CSVFormat.DEFAULT)
        csvPrinter.printRecords(headerTitles)
        rows.map { it -> csvPrinter.printRecord(it.data) }

        return ByteArrayInputStream(csvContent.toString().toByteArray())
    }
}
