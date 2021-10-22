package apply.ui.admin.selections

import apply.application.AssignmentData
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField

class AssignmentForm(assignmentData: AssignmentData) : FormLayout() {
    init {
        add(
            H3("과제 제출물"),
            TextField("Github Username").apply {
                value = assignmentData.githubUsername
                isReadOnly = true
            },
            createUrlField(assignmentData.pullRequestUrl),
            TextArea("소감").apply {
                value = assignmentData.note
                isReadOnly = true
            }
        )
        setResponsiveSteps(ResponsiveStep("0", 1))
    }

    private fun createUrlField(url: String): Component {
        return HorizontalLayout(
            TextField("Pull Request URL").apply {
                value = url
                isReadOnly = true
                setWidthFull()
            }
        ).apply {
            addClickListener { UI.getCurrent().page.open(url) }
        }
    }
}
