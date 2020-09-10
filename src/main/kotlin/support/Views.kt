package support

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.KeyDownEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer
import java.time.LocalDate
import java.time.LocalDateTime

typealias ClickListener = (ClickEvent<Button>) -> Unit

fun createPrimaryButton(text: String, clickListener: ClickListener): Button {
    return Button(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
    }
}

fun createSuccessButton(text: String, clickListener: ClickListener): Button {
    return createPrimaryButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_SUCCESS)
    }
}

fun createPrimarySmallButton(text: String, clickListener: ClickListener): Button {
    return createPrimaryButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_SMALL)
    }
}

fun createSuccessSmallButton(text: String, clickListener: ClickListener): Button {
    return createPrimarySmallButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_SUCCESS)
    }
}

fun createErrorSmallButton(text: String, clickListener: ClickListener): Button {
    return createPrimarySmallButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_ERROR)
    }
}

fun createContrastSmallButton(text: String, clickListener: ClickListener): Button {
    return createPrimarySmallButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_CONTRAST)
    }
}

fun createSuccessButtonWithDialog(text: String, message: String, clickListener: ClickListener): Button {
    return createSuccessSmallButton(text) { createConfirmDialog(message, clickListener).open() }
}

fun createContrastButtonWithDialog(text: String, message: String, clickListener: ClickListener): Button {
    return createContrastSmallButton(text) { createConfirmDialog(message, clickListener).open() }
}

fun createDeleteButtonWithDialog(message: String, clickListener: ClickListener): Button {
    return createErrorSmallButton("삭제") { createConfirmDialog(message, clickListener).open() }
}

fun createConfirmDialog(
    text: String,
    confirmListener: ClickListener,
    cancelListener: ClickListener = {}
): Dialog {
    return Dialog(Text(text)).apply {
        add(
            HorizontalLayout(
                createCancelButton(cancelListener),
                createConfirmButton(confirmListener)
            ).apply {
                justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            }
        )
    }
}

private fun Dialog.createCancelButton(clickListener: ClickListener): Button {
    return Button("취소") {
        clickListener(it)
        this.close()
    }
}

private fun Dialog.createConfirmButton(clickListener: ClickListener): Button {
    return createPrimaryButton("확인") {
        clickListener(it)
        UI.getCurrent().page.reload()
    }
}

fun <T : Any> Grid<T>.addSortableColumn(labelText: String, valueProvider: (T) -> Any): Grid.Column<T> {
    return addColumn(valueProvider).apply {
        addSortableHeader(labelText)
    }
}

fun <T : Any> Grid<T>.addSortableDateTimeColumn(
    labelText: String,
    valueProvider: (T) -> LocalDateTime
): Grid.Column<T> {
    return addColumn(LocalDateTimeRenderer(valueProvider, "yyyy-MM-dd HH:mm:ss")).apply {
        addSortableHeader(labelText)
        setComparator(compareBy(valueProvider))
    }
}

fun <T : Any> Grid<T>.addSortableDateColumn(
    labelText: String,
    valueProvider: (T) -> LocalDate
): Grid.Column<T> {
    return addColumn(LocalDateRenderer(valueProvider, "yyyy-MM-dd")).apply {
        addSortableHeader(labelText)
        setComparator(compareBy(valueProvider))
    }
}

private fun <T : Any> Grid.Column<T>.addSortableHeader(labelText: String) {
    setHeader(labelText)
    isSortable = true
    isAutoWidth = true
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
