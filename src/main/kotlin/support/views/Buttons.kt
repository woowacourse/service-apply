package support.views

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

typealias ClickListener = (ClickEvent<Button>) -> Unit

fun createNormalButton(text: String, clickListener: ClickListener): Button {
    return Button(text, clickListener)
}

fun createPrimaryButton(text: String = "", clickListener: ClickListener): Button {
    return Button(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
    }
}

fun createSuccessButton(text: String, clickListener: ClickListener): Button {
    return createPrimaryButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_SUCCESS)
    }
}

fun createErrorButton(text: String, clickListener: ClickListener): Button {
    return createPrimaryButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_ERROR)
    }
}

fun createContrastButton(text: String, clickListener: ClickListener): Button {
    return createPrimaryButton(text, clickListener).apply {
        addThemeVariants(ButtonVariant.LUMO_CONTRAST)
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

private fun createConfirmDialog(
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
