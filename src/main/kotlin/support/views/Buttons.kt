package support.views

import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer

typealias ClickListener = (ClickEvent<Button>) -> Unit
typealias UploadFinishedListener = (MemoryBuffer) -> Unit
typealias UploadSucceededListener = (MultiFileMemoryBuffer) -> Unit

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

fun createUpload(
    text: String,
    receiver: MultiFileMemoryBuffer,
    succeededListener: UploadSucceededListener
): Upload {
    return Upload(receiver).apply {
        maxFileSize = 3_145_728 // spring.servlet.multipart.max-file-size
        uploadButton = createPrimaryButton(text) { }
        addSucceededListener {
            succeededListener(receiver)
        }
    }
}

fun createCsvUpload(text: String, receiver: MemoryBuffer, finishedListener: UploadFinishedListener): Upload {
    return Upload(receiver).apply {
        setAcceptedFileTypes("text/csv")
        uploadButton = createPrimaryButton(text) {}
        addFinishedListener {
            finishedListener(receiver)
        }
        addFailedListener {
            createNotification(it.reason.localizedMessage)
        }
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

fun createSuccessButtonWithDialog(
    text: String,
    message: String,
    clickListener: ClickListener,
    successListener: Dialog.() -> Unit = { close() }
): Button {
    return createSuccessSmallButton(text) { createConfirmDialog(message, clickListener, successListener) }
}

fun createContrastButtonWithDialog(
    text: String,
    message: String,
    successListener: Dialog.() -> Unit = { close() },
    clickListener: ClickListener
): Button {
    return createContrastButton(text) { createConfirmDialog(message, clickListener, successListener) }
}

fun createDeleteButtonWithDialog(
    message: String,
    successListener: Dialog.() -> Unit = { UI.getCurrent().page.reload() },
    clickListener: ClickListener
): Button {
    return createErrorSmallButton("삭제") { createConfirmDialog(message, clickListener, successListener) }
}

private fun createConfirmDialog(
    text: String,
    confirmListener: ClickListener,
    successListener: Dialog.() -> Unit
): Dialog {
    return Dialog(Text(text)).apply {
        add(
            HorizontalLayout(
                createCancelButton(),
                createConfirmButton(confirmListener, successListener)
            ).apply {
                justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            }
        )
        open()
    }
}

private fun Dialog.createCancelButton(): Button {
    return Button("취소") {
        this.close()
    }
}

private fun Dialog.createConfirmButton(clickListener: ClickListener, successListener: Dialog.() -> Unit): Button {
    return createPrimaryButton("확인") {
        runCatching { clickListener(it) }
            .onSuccess { successListener() }
            .onFailure { e -> createNotification(e.localizedMessage) }
    }
}
