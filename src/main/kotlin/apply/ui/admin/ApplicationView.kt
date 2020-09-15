package apply.ui.admin

import apply.application.ApplicationService
import apply.application.RecruitmentItemService
import apply.domain.application.Application
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter

@Route(value = "admin", layout = BaseLayout::class)
class ApplicationView(
    private val applicationService: ApplicationService,
    private val recruitmentItemService: RecruitmentItemService
) : VerticalLayout(), HasUrlParameter<String> {
    private var recruitmentId: Long = 0L

    private fun createRecruitmentItems(application: Application): Array<Component> {
        val recruitmentItems = recruitmentItemService.findByRecruitmentId(recruitmentId)
            .map { it.id to it.title }
            .toMap()
        return application.answers
            .items
            .map {
                createQnAs(recruitmentItems[it.recruitmentItemId] ?: throw IllegalArgumentException(), it.contents)
            }.toTypedArray()
    }

    private fun createQnAs(title: String, contents: String): Component {
        return Div(createQuestion(title), createAnswer(contents)).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createQuestion(title: String): Component {
        return HorizontalLayout(H3(title)).apply {
            setSizeFull()
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    private fun createAnswer(answer: String): Component {
        return TextArea().apply {
            setSizeFull()
            isReadOnly = true
            value = answer
            justifyContentMode = FlexComponent.JustifyContentMode.START
        }
    }

    override fun setParameter(event: BeforeEvent?, @WildcardParameter parameter: String) {
        val parameters = parameter.split("/")
        this.recruitmentId = parameters[1].toLong()
        val applicantId = parameters[3].toLong()
        add(*createRecruitmentItems(applicationService.getByRecruitmentIdAndApplicantId(recruitmentId, applicantId)))
    }
}
