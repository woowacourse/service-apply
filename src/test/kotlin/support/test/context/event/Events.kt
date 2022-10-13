package support.test.context.event

import org.springframework.context.event.EventListener

class Events {
    private val _events: MutableList<Any> = mutableListOf()
    val events: List<Any>
        get() = _events

    @EventListener
    internal fun addEvent(event: Any) {
        _events.add(event)
    }

    inline fun <reified T : Any> events(): List<T> = events.filterIsInstance(T::class.java)
    inline fun <reified T : Any> count(): Int = events<T>().count()
    inline fun <reified T : Any> first(): T = events<T>().first()
    fun clear() = _events.clear()
}
