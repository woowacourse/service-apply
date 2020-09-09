package apply.ui.admin

import apply.application.ApplicantService
import apply.domain.applicant.Applicant
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L

    init {
        add(createTitle(), createMenu(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("웹 백엔드 3기")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMenu(): Component {
        return HorizontalLayout(Div(TextField(), Button(Icon(VaadinIcon.SEARCH))), Button("다운로드")).apply {
            setSizeFull()
            isSpacing = false
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createGrid(): Component {
        return Grid(Applicant::class.java).apply {
            setColumns("name", "email", "phoneNumber", "gender", "birthday")
            setItems(applicantService.findAll())
        }
    }

    override fun setParameter(event: BeforeEvent?, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
    }
}
