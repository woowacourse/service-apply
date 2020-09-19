package support

import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> getMemberPropertiesByDeclaredOrder(t: T): List<Any?> {
    val fieldNamesByDeclaredOrder = T::class.primaryConstructor!!
        .parameters
        .map { it.name!! }
    val memberProperties = T::class.memberProperties

    return fieldNamesByDeclaredOrder.map { fieldName -> memberProperties.first { it.name == fieldName }.get(t) }
}
