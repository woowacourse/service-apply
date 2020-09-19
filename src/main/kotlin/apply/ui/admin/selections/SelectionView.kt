package apply.ui.admin.selections

import apply.application.ApplicantService
import apply.application.DownloadService
import apply.application.RecruitmentService
import apply.domain.applicant.Applicant
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.server.VaadinSession
import support.views.addSortableColumn
import support.views.addSortableDateColumn
import support.views.createPrimarySmallButton
import support.views.createSearchBar
import support.views.createSuccessButton

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService,
    private val downloadService: DownloadService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId: Long = 0L

    private fun createTitle(): Component {
        return HorizontalLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createMenu(): Component {
        return HorizontalLayout(
            createSearchBar {
                removeAll()
                add(createTitle(), createMenu(), createGrid(applicantService.findByValue(it)))
            },
            createSuccessButton("다운로드") {
                val excel = { downloadService.createExcelBy(recruitmentId) }
                val registration = VaadinSession.getCurrent()
                    .resourceRegistry
                    .registerResource(StreamResource("${recruitmentService.getById(recruitmentId).title}.xlsx", excel))
                UI.getCurrent().page.setLocation(registration.resourceUri)
            }
        ).apply {
            setSizeFull()
            isSpacing = false
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createGrid(applicants: List<Applicant>): Component {
        return Grid<Applicant>(10).apply {
            addSortableColumn("이름", Applicant::name)
            addSortableColumn("이메일", Applicant::email)
            addSortableColumn("전화번호", Applicant::phoneNumber)
            addSortableColumn("성별") { it.gender.title }
            addSortableDateColumn("생년월일", Applicant::birthday)
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(applicants)
        }
    }

    private fun createButtonRenderer(): Renderer<Applicant> {
        return ComponentRenderer<Component, Applicant> { applicant ->
            createPrimarySmallButton("지원서") { applicant.id }
        }
    }

    override fun setParameter(event: BeforeEvent?, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        // Todo: RecruitmentId 별로 지원자를 불러오도록 수정
        add(createTitle(), createMenu(), createGrid(applicantService.findAll()))
    }
}
