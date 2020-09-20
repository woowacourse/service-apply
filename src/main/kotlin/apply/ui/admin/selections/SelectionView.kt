package apply.ui.admin.selections

import apply.application.ApplicantService
import apply.application.RecruitmentService
import apply.domain.applicant.ApplicantResponse
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
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
import support.views.addSortableColumn
import support.views.addSortableDateColumn
import support.views.createPrimarySmallButton
import support.views.createSearchBar
import support.views.createSuccessButton

@Route(value = "admin/selections", layout = BaseLayout::class)
class SelectionView(
    private val applicantService: ApplicantService,
    private val recruitmentService: RecruitmentService
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
                // Todo: 엑셀 다운로드
            }
        ).apply {
            setSizeFull()
            isSpacing = false
            justifyContentMode = FlexComponent.JustifyContentMode.BETWEEN
        }
    }

    private fun createGrid(applicants: List<ApplicantResponse>): Component {
        return Grid<ApplicantResponse>(10).apply {
            addSortableColumn("이름", ApplicantResponse::name)
            addSortableColumn("이메일", ApplicantResponse::email)
            addSortableColumn("전화번호", ApplicantResponse::phoneNumber)
            addSortableColumn("성별") { it.gender.title }
            addSortableDateColumn("생년월일", ApplicantResponse::birthday)
            addSortableColumn("부정 행위자") { if (it.isCheater) "O" else "X" }
            addColumn(createButtonRenderer()).apply { isAutoWidth = true }
            setItems(applicants)
        }
    }

    private fun createButtonRenderer(): Renderer<ApplicantResponse> {
        return ComponentRenderer<Component, ApplicantResponse> { applicant ->
            createPrimarySmallButton("지원서") { applicant.id }
        }
    }

    override fun setParameter(event: BeforeEvent?, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        // Todo: RecruitmentId 별로 지원자를 불러오도록 수정
        add(createTitle(), createMenu(), createGrid(applicantService.findAll()))
    }
}
