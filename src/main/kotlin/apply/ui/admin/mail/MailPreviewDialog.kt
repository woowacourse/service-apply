package apply.ui.admin.mail

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import org.jsoup.Jsoup
import support.views.createContrastButton

class MailPreviewDialog(
    htmlText: String
) : Dialog() {
    init {
        add(createHeader(), createContent(htmlText), createButtons())
        width = "700px"
        height = "800px"
        open()
    }

    private fun createHeader(): VerticalLayout {
        return VerticalLayout(H2("메일 미리 보기")).apply {
            isPadding = false
            element.style["margin-bottom"] = "10px"
        }
    }

    private fun createContent(htmlText: String): Component {
        val body = Jsoup.parse(htmlText).body()
        return Html(body.html()).apply {
            element.style["display"] = "block"
            element.style["height"] = "600px"
            element.style["overflow"] = "auto"
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createCloseButton()).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style["margin-top"] = "20px"
        }
    }

    private fun createCloseButton(): Button {
        return createContrastButton("닫기") {
            close()
        }
    }
}
