package support.domain

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class StringToListConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(recipients: List<String>): String {
        return recipients.joinToString(COMMA)
    }

    override fun convertToEntityAttribute(dbData: String): List<String> {
        return dbData.split(COMMA)
    }

    companion object {
        private const val COMMA: String = ","
    }
}
