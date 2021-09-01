package apply.utils

import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream

@Component
class CsvGenerator {
    fun generateBy(headerTitles: Array<String>, rows: List<CsvRow>): ByteArrayInputStream {
        val data = ""

        val csvContent = headerTitles.joinToString(",")
        return ByteArrayInputStream(csvContent.toByteArray())
    }
}
