package apply.ui.admin.selections

import apply.application.AssignmentData
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingFormLayout

class AssignmentForm() : BindingFormLayout<AssignmentData>(AssignmentData::class) {
    private val githubUsername: TextField = TextField("Github Username")
    private val pullRequestUrl: TextField = TextField("Pull Request URL")
    private val note: TextArea = TextArea("소감")
    private val title: H3 = H3("과제 제출물")

    init {
        add(title, githubUsername, pullRequestUrl, note)
        setResponsiveSteps(ResponsiveStep("0", 1))
    }

    override fun bindOrNull(): AssignmentData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: AssignmentData) {
        fillDefault(data)
        toReadOnlyMode()
    }

    private fun toReadOnlyMode() {
        this.githubUsername.isReadOnly = true
        this.pullRequestUrl.isReadOnly = true
        this.note.isReadOnly = true
    }
}
