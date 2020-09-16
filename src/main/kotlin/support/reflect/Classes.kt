package support.reflect

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

inline fun <reified T : Any> KClass<T>.newInstance(): T {
    checkNotNull(findAnnotation<NoArgsConstructor>()) { "Class should be annotated with NoArgsConstructor: $this" }
    check(hasNoArgsConstructor()) { "Class already has a no-argument constructor: $this" }
    return java.getDeclaredConstructor().newInstance()
}

inline fun <reified T : Any> KClass<T>.hasNoArgsConstructor() = java.constructors.any { it.parameterCount == 0 }
