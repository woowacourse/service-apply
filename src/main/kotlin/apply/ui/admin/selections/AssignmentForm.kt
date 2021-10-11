package apply.ui.admin.selections

import apply.application.AssignmentData
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import support.views.BindingFormLayout

class AssignmentForm() : BindingFormLayout<AssignmentData>(AssignmentData::class) {
    private val githubUsername: TextField = TextField("Github 유저 네임")
    private val pullRequestUrl: TextField = TextField("PR URL")
    private val note: TextArea = TextArea("소감")

    init {
        add(githubUsername, pullRequestUrl, note)
        setResponsiveSteps(ResponsiveStep("0", 1))
    }

    override fun bindOrNull(): AssignmentData? {
        return bindDefaultOrNull()
    }

    override fun fill(data: AssignmentData) {
        fillDefault(data)
    }
}
