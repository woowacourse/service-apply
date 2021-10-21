package apply.ui.admin.selections

import apply.application.AssignmentData
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.H3
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
            TextField("Pull Request URL").apply {
                value = assignmentData.pullRequestUrl
                isReadOnly = true
            },
            TextArea("소감").apply {
                value = assignmentData.note
                isReadOnly = true
            }
        )
        setResponsiveSteps(ResponsiveStep("0", 1))
    }
}
