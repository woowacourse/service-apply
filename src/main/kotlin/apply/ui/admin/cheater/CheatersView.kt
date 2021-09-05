package apply.ui.admin.cheater

import apply.application.ApplicantResponse
import apply.application.ApplicantService
import apply.application.CheaterResponse
import apply.application.CheaterService
import apply.ui.admin.BaseLayout
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.router.Route
import support.views.addSortableColumn
import support.views.addSortableDateTimeColumn
import support.views.createDeleteButtonWithDialog
import support.views.createPrimaryButton
import support.views.createSearchBar

@Route(value = "admin/cheaters", layout = BaseLayout::class)
class CheatersView(
    private val applicantService: ApplicantService,
    private val cheaterService: CheaterService
) : VerticalLayout() {
    init {
        add(createTitle(), createAddCheater(), createCheaterGrid())
    }

    private fun createTitle(): Component {
        return HorizontalLayout(H1("부정 행위자")).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createAddCheater(): Component {
        val container = HorizontalLayout()
        return HorizontalLayout(
            createSearchBar {
                container.removeAll()
                val founds = applicantService.findAllByKeyword(it)
                if (founds.isNotEmpty()) {
                    val select = createSelectApplicant(founds)
                    container.add(
                        select,
                        createPrimaryButton("추가") {
                            /* 부정행위자 입력 폼을 구현한다.
                                - 현황
                                    - 현재 해당하는 이메일이 검색되면 추가하는 버튼이 생겨 이메일을 추가할 수 있다.
                                    - 해당 이메일만 추가하게 뷰가 구현되어 있다.
                                - 변경 필요사항
                                    - 추가 버튼을 누르면 입력 폼이 나오고, cheaterService.save 로 해당 폼의 내용이 전달되게 한다.
                                    - CheaterDtos 에 해당 Form 에서 사용될 Dto 가 설정되어 있다.
                                - 기존코드
                                   cheaterService.save(select.value.email)
                            */
                            UI.getCurrent().page.reload()
                        }
                    )
                }
            },
            container
        ).apply { setSizeFull() }
    }

    private fun createSelectApplicant(applicants: List<ApplicantResponse>): Select<ApplicantResponse> {
        return Select<ApplicantResponse>().apply {
            setTextRenderer { "${it.name}/${it.email}" }
            setItems(applicants)
        }
    }

    private fun createCheaterGrid(): Grid<CheaterResponse> {
        return Grid<CheaterResponse>(10).apply {
            addSortableColumn("이름") { it.applicant.name }
            addSortableColumn("이메일") { it.applicant.email }
            addSortableDateTimeColumn("등록일", CheaterResponse::createdDateTime)
            addColumn(createDeleteButtonRenderer()).apply { isAutoWidth = true }
            setItems(cheaterService.findAll())
        }
    }

    private fun createDeleteButtonRenderer(): Renderer<CheaterResponse> {
        return ComponentRenderer<Component, CheaterResponse> { cheater ->
            createDeleteButtonWithDialog("부정 행위자를 삭제하시겠습니까?") {
                cheaterService.deleteById(cheater.id)
            }
        }
    }
}
