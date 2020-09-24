package support.views

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.KeyDownEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.ComponentRenderer

fun createIntSelect(min: Int = 0, max: Int): Select<Int> {
    return Select(*(min..max).toList().toTypedArray())
}

fun createBooleanRadioButtonGroup(
    labelText: String,
    trueText: String = true.toString(),
    falseText: String = false.toString(),
    defaultValue: Boolean = false
): RadioButtonGroup<Boolean> {
    return RadioButtonGroup<Boolean>().apply {
        setItems(true, false)
        label = labelText
        value = defaultValue
        setRenderer(createTextRenderer(trueText, falseText))
    }
}

private fun createTextRenderer(trueText: String, falseText: String): ComponentRenderer<Component, Boolean> {
    return ComponentRenderer { it ->
        if (it) {
            Text(trueText)
        } else {
            Text(falseText)
        }
    }
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
