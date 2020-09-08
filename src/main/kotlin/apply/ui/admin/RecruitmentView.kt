package apply.ui.admin

import apply.application.RecruitmentService
import apply.domain.recruitment.Recruitment
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route(value = "admin/recruitment", layout = BaseLayout::class)
class RecruitmentView(private val recruitmentService: RecruitmentService) : VerticalLayout() {
    init {
        add(createTitle(), createButton(), createGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("모집 관리")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createButton(): Component {
        return HorizontalLayout(Button("생성")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.END
        }
    }

    private fun createGrid(): Component {
        return Grid(Recruitment::class.java).apply {
            setColumns("title", "status", "startDateTime", "endDateTime")
            setItems(recruitmentService.findAll())
        }
    }
}
