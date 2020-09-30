package support.views

import com.vaadin.flow.component.textfield.AbstractNumberField
import com.vaadin.flow.function.SerializableFunction

private val longToStringFormatter = SerializableFunction { v: Long? -> v?.toString() ?: "" }
private val stringToLongFormatter = SerializableFunction { v: String? -> v?.toLong() }

class LongField : AbstractNumberField<LongField, Long>(
    stringToLongFormatter,
    longToStringFormatter,
    Long.MIN_VALUE.toDouble(),
    Long.MAX_VALUE.toDouble()
)
