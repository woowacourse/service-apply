package support.views

import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.KeyDownEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField

fun createIntSelect(min: Int = 0, max: Int): Select<Int> {
    return Select(*(min..max).toList().toTypedArray())
}

fun <T> createItemSelect(title: String): Select<T> {
    val select: Select<T> = Select()
    select.apply {
        label = title
    }
    return select
}

fun createSearchBar(eventListener: (name: String) -> Unit): Div {
    val textField = TextField()
    textField.addKeyDownListener(
        Key.ENTER,
        ComponentEventListener<KeyDownEvent?> { eventListener(textField.value) }
    )
    return Div(
        textField,
        Button(Icon(VaadinIcon.SEARCH)) { eventListener(textField.value) }
    )
}
