package support.domain

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class StringToLongListConverter : AttributeConverter<List<Long>, String> {
    override fun convertToDatabaseColumn(recipients: List<Long>): String {
        return recipients.joinToString(COMMA)
    }

    override fun convertToEntityAttribute(dbData: String): List<Long> {
        return dbData.split(COMMA).map { it.toLong() }
    }

    companion object {
        private const val COMMA: String = ","
    }
}
