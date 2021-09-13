package apply.ui.admin.mail

import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import support.views.Title
import support.views.createPrimaryButton

@Route(value = "admin/emails", layout = BaseLayout::class)
class MailSelectionsView : VerticalLayout() {
    init {
        add(Title("메일 발송"), *createMailTypeButtons())
    }

    private fun createMailTypeButtons(): Array<Component> {
        return arrayOf(createGroupMailButton(), createIndividualMailButton())
    }

    private fun createGroupMailButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("그룹 발송") {
                UI.getCurrent().navigate(GroupMailFormView::class.java)
            }
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createIndividualMailButton(): Component {
        return HorizontalLayout(
            createPrimaryButton("개별 발송") {
                UI.getCurrent().navigate(IndividualMailFormView::class.java)
            }
        ).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }
}
