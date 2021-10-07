package apply.ui.admin.mail

import apply.application.mail.MailData
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout

class MailHistoryDialog(
    mailData: MailData
) : Dialog() {
    private val subject: H2 = H2()
    private val sender: H2 = H2()
    private val body: H2 = H2()
    private val recipients: H2 = H2()
    private val sentTime: H2 = H2()

    init {
        subject.text = mailData.subject
        sender.text = mailData.sender
        body.text = mailData.body
        recipients.text = mailData.recipients.toString()
        sentTime.text = mailData.sentTime.toString()

        add(createHeader())
        width = "800px"
        height = "90%"
        open()
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(subject, sender, body, recipients, sentTime).apply {
            alignItems = FlexComponent.Alignment.CENTER
            isPadding = false
            element.style.set("margin-bottom", "20px")
        }
    }
}
