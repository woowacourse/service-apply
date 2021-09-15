package apply.ui.admin.mail

import apply.application.MailTargetService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.html.H4
import com.vaadin.flow.component.orderedlayout.FlexComponent
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

    protected fun createMailForm(): Component {
        val titleText = VerticalLayout(
            H3("메일 제목"),
            title.apply { setSizeFull() }
        )

        val receivers = VerticalLayout(H4("수신자"), createReceiverFilter())
        val mailBody = VerticalLayout(
            content
        ).apply {
            setSizeFull()
        }

        val sendButton = VerticalLayout(
            createPrimaryButton("전송") {
                println("title: ${title.value}  receivers: ${this.receivers}  content: ${this.content.value}")
                // todo: (print 내용 지우고) mailSender로 메일 보내기!
            }
        ).apply {
            setSizeFull()
        }

        return VerticalLayout(titleText, receivers, mailBody, sendButton).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            defaultHorizontalComponentAlignment = FlexComponent.Alignment.CENTER
        }
    }

    private fun createMailBody(): TextArea {
        return TextArea("메일 본문").apply {
            setSizeFull()
            style.set("minHeight", "800px")
            placeholder = "메일 본문 입력"
        }
    }

    abstract fun createReceiverFilter(): Component
}
