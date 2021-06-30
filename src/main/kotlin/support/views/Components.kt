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
import com.vaadin.flow.component.orderedlayout.FlexLayout
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
        HorizontalLayout(
            textField,
            Button(Icon(VaadinIcon.SEARCH)) { eventListener(textField.value) }
        )
    )
}

fun createTextBox(text: String, fontSize: Int = 16, fontWeight: String = "500", color: Color = Color.PRIMARY): Div {
    return Div(Text(text)).apply {
        element.style.set("margin", "0")
        element.style.set("font-size", "${fontSize}px")
        element.style.set("font-weight", fontWeight)
        element.style.set("color", color.hex)
    }
}

fun createCard(
    vararg components: Component,
    direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.ROW
): FlexLayout {
    return FlexLayout(*components).apply {
        element.style.set("margin", "5px")
        element.style.set("padding", "20px 15px")
        element.style.set("background-color", Color.BLACK.hex)
        element.style.set("box-shadow", "1px 1px 3px ${Color.GRAY2.hex}")
        setFlexDirection(direction)
    }
}

fun createHorizontalDivider(ratio: Int): Div {
    return Div().apply {
        element.style.set("background-color", Color.GRAY2.hex)
        element.style.set("flex", ratio.toString())
        height = "2px"
    }
}

class Title(val value: H1) : HorizontalLayout(), HasText {
    init {
        add(value)
        setSizeFull()
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
    }

    constructor(text: String = "") : this(H1(text))

    override fun setText(text: String) {
        value.text = text
    }

    override fun getText(): String {
        return value.text
    }
}
