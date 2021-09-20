package apply.ui.admin.mail

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.router.RoutePrefix
import support.views.createPrimaryButton
import support.views.createUploadButton

@RoutePrefix(value = "admin/emails")
abstract class MailFormView(
    private val senderEmail: String
) : VerticalLayout() {
    protected val subject: TextField = TextField("메일 제목", "메일 제목 입력")
    protected val recipients: MutableList<String> = mutableListOf()
    protected val body: TextArea = createMailBody()
    private val currentRecipients = HorizontalLayout()

    abstract fun createRecipientFilter(): Component

    protected fun createMailForm(): Component {
        val subjectText = VerticalLayout(
            H3("메일 제목"),
            subject.apply { setSizeFull() }
        )

        val sender = VerticalLayout(H4("보낸사람"), createSender())
        val recipientFilter = VerticalLayout(H4("받는사람"), createRecipientFilter())

        val mailBody = VerticalLayout(
            body
        ).apply {
            setSizeFull()
        }

        val uploadFile = createUploadButton("첨부파일", MultiFileMemoryBuffer()) {
            /*
            todo: 추후 업로드 된 파일을 메일로 첨부하는 로직이 추가되어야 함
             (uploadFiles 같은 필드를 두고 mail을 보내는 기능에 포함시키면 될 것 같음)
            it.files.forEach { fileName ->
                val fileData = it.getFileData(fileName)
                val inputStream = it.getInputStream(fileName)
                val readBytes = inputStream.readBytes()
            }
            */
        }.apply {
            setWidthFull()
        }

        val sendButton = createMailSendButton()

        return VerticalLayout(
            subjectText,
            sender,
            recipientFilter,
            VerticalLayout(currentRecipients.apply { setWidthFull() }).apply { },
            mailBody,
            uploadFile,
            sendButton
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun createSender(): Component {
        val sender = TextField().apply {
            value = senderEmail
            isReadOnly = true
            style.set("background-color", "#00B493")
        }
        return HorizontalLayout(sender)
    }

    private fun createMailSendButton(): VerticalLayout {
        return VerticalLayout(
            createPrimaryButton("전송") {
                // todo: emailService.메일전송(subject.value, recipients, body.value)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    protected fun addRecipientComponent(email: String) {
        this.currentRecipients.apply {
            recipients.add(email)
            add(createRecipientComponent(email))
        }
    }

    protected fun clearCurrentRecipients() {
        this.currentRecipients.removeAll()
        this.recipients.clear()
    }

    protected fun removeRecipientComponent(email: String) {
        recipients.remove(email)
        addAllCurrentRecipientComponent()
    }

    private fun addAllCurrentRecipientComponent() {
        this.currentRecipients.removeAll()
        this.currentRecipients.apply {
            recipients.forEach {
                add(createRecipientComponent(it))
            }
        }
    }

    private fun createMailBody(): TextArea {
        return TextArea("메일 본문").apply {
            setSizeFull()
            style.set("minHeight", "400px")
            placeholder = "메일 본문 입력"
        }
    }

    private fun createRecipientComponent(email: String): Component {
        val emailTarget = TextField().apply {
            value = email
            isReadOnly = true
            style.set("background-color", "#00B493")
        }

        return Span(
            emailTarget,
            Button(Icon(VaadinIcon.CLOSE_SMALL)) {
                removeRecipientComponent(email)
            }
        )
    }
}
