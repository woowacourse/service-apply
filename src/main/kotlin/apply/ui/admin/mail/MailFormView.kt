package apply.ui.admin.mail

import apply.application.MailTargetService
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
import com.vaadin.flow.router.RoutePrefix
import support.views.createPrimaryButton

@RoutePrefix(value = "admin/emails")
abstract class MailFormView(
    private val mailTargetService: MailTargetService
) : VerticalLayout() {
    protected val title: TextField = TextField("메일 제목", "메일 제목 입력")
    protected val receivers: MutableList<String> = mutableListOf()
    protected val content: TextArea = createMailBody()
    private val currentReceivers = HorizontalLayout()

    abstract fun createReceiverFilter(): Component

    protected fun createMailForm(): Component {
        val titleText = VerticalLayout(
            H3("메일 제목"),
            title.apply { setSizeFull() }
        )

        val receiverFilter = VerticalLayout(H4("수신자"), createReceiverFilter())
        val mailBody = VerticalLayout(
            content
        ).apply {
            setSizeFull()
        }

        val sendButton = createMailSendButton()

        return VerticalLayout(
            titleText,
            receiverFilter,
            currentReceivers.apply { setSizeFull() },
            mailBody,
            sendButton
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun createMailSendButton(): VerticalLayout {
        return VerticalLayout(
            createPrimaryButton("전송") {
                println("title: ${title.value}  receivers: ${this.receivers}  content: ${this.content.value}")
                // todo: emailService.메일전송(title.value, receivers, content.value)
            }
        ).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    protected fun addReceiverComponent(email: String) {
        this.currentReceivers.apply {
            receivers.add(email)
            add(createReceiverComponent(email))
        }
    }

    protected fun clearCurrentReceivers() {
        this.currentReceivers.removeAll()
        this.receivers.clear()
    }

    protected fun removeReceiverComponent(email: String) {
        receivers.remove(email)
        addAllCurrentReceiverComponent()
    }

    private fun addAllCurrentReceiverComponent() {
        this.currentReceivers.removeAll()
        this.currentReceivers.apply {
            receivers.forEach {
                add(createReceiverComponent(it))
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

    private fun createReceiverComponent(email: String): Component {
        val emailTarget = TextField().apply {
            value = email
            isReadOnly = true
            style.set("background-color", "#00B493")
        }

        return Span(
            emailTarget,
            Button(Icon(VaadinIcon.CLOSE_SMALL)) {
                removeReceiverComponent(email)
            }
        )
    }
}
