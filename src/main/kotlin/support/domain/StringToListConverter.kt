package support.domain

import javax.persistence.AttributeConverter
import javax.persistence.Converter
import kotlin.reflect.KClass

abstract class StringToListConverter<T : Any>(
    private val type: KClass<T>,
    private val transform: (String) -> T,
) : AttributeConverter<List<T>, String> {
    override fun convertToDatabaseColumn(attribute: List<T>): String {
        return attribute.joinToString(COMMA)
    }

    override fun convertToEntityAttribute(dbData: String): List<T> {
        return dbData.split(COMMA).map { transform(it) }
    }

    companion object {
        private const val COMMA: String = ","
    }
}

@Converter
class StringToLongListConverter : StringToListConverter<Long>(Long::class, String::toLong)
