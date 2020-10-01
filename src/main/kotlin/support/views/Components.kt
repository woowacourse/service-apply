package support.views

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HasText
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.KeyDownEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.ComponentRenderer

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

class Title(val value: H1) : HorizontalLayout(), HasText {
    init {
        add(value)
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
    }

    constructor(text: String) : this(H1(text))

    override fun setText(text: String) {
        value.text = text
    }

    override fun getText(): String {
        return value.text
    }
}
