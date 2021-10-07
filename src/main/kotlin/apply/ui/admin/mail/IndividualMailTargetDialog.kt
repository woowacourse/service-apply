package apply.ui.admin.mail

import apply.application.MailTargetResponse
import apply.application.UserResponse
import apply.application.UserService
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import support.views.addSortableColumn
import support.views.createContrastButton
import support.views.createPrimarySmallButton
import support.views.createSearchBox

class IndividualMailTargetDialog(
    private val userService: UserService,
    private val accept: (MailTargetResponse) -> Unit
) : Dialog() {
    private val mailTargetsGrid: Grid<UserResponse> = createMailTargetsGrid()

    init {
        add(H2("개별 불러오기"), createSearchFilter(), mailTargetsGrid, createButtons())
        width = "900px"
        height = "70%"
        open()
    }

    private fun createSearchFilter(): Component {
        return HorizontalLayout(
            createSearchBox { mailTargetsGrid.setItems(userService.findAllByKeyword(it)) }
        ).apply {
            element.style.set("margin-top", "10px")
            element.style.set("margin-bottom", "10px")
        }
    }

    private fun createMailTargetsGrid(): Grid<UserResponse> {
        return Grid<UserResponse>(10).apply {
            addSortableColumn("이름", UserResponse::name)
            addSortableColumn("이메일", UserResponse::email)
            addColumn(createAddButton()).apply { isAutoWidth = true }
        }
    }

    private fun createAddButton(): Renderer<UserResponse> {
        return ComponentRenderer<Component, UserResponse> { applicantResponse ->
            createPrimarySmallButton("추가") {
                accept(MailTargetResponse(applicantResponse))
            }.apply {
                isDisableOnClick = true
            }
        }
    }

    private fun createButtons(): Component {
        return HorizontalLayout(createCancelButton()).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
            element.style.set("margin-top", "10px")
        }
    }

    private fun createCancelButton(): Button {
        return createContrastButton("취소") {
            close()
        }
    }
}
