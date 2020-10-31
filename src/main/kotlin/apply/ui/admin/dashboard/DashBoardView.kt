package apply.ui.admin.dashboard

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.config.legend.Position
import com.github.appreciated.apexcharts.helper.Series
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter
import support.views.createCard
import support.views.createChart
import support.views.createTextBox

@Route(value = "admin/dashboard", layout = BaseLayout::class)
class DashBoardView(
    private val recruitmentService: RecruitmentService,
    private val applicantService: ApplicantService
) : FlexLayout(), HasUrlParameter<Long> {
    private var recruitmentId = 0L

    init {
        setFlexDirection(FlexDirection.COLUMN)
        element.style.set("background-color", "#f1f2f6")
    }

    private fun createTitle(): Component {
        return FlexLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createContent(): Component {
        val applicants = applicantService.findAllByRecruitmentId(recruitmentId)
        val submittedApplicants = applicants.filter { it.applicationForm.submitted }

        return FlexLayout(
            createStatistics(applicants, "지원서"),
            createStatistics(submittedApplicants, "제출한 사람")
        )
    }

    private fun createStatistics(applicants: List<ApplicantAndFormResponse>, description: String): FlexLayout {
        return FlexLayout(
            createSummary("${applicants.count()}명", description, Icon(VaadinIcon.TAG)),
            createYearApplicant(applicants),
            createGenderApplicant(applicants)
        ).apply {
            setFlexDirection(FlexDirection.COLUMN)
            element.style.set("flex", "1")
        }
    }

    private fun createYearApplicant(applicants: List<ApplicantAndFormResponse>): Component {
        val birthdayGroup = applicants.groupingBy { it.birthday.year.toString() }.eachCount()

        return createChart(
            title = "나이",
            type = Type.bar,
            series = Series("지원자", *birthdayGroup.values.toTypedArray()),
            labels = birthdayGroup.keys
        )
    }

    private fun createGenderApplicant(applicants: List<ApplicantAndFormResponse>): Component {
        val genderGroup = applicants.groupingBy { it.gender.title }.eachCount()

        return createChart(
            title = "성비",
            type = Type.pie,
            series = genderGroup.values.map { it.toDouble() },
            labels = genderGroup.keys,
            legendPosition = Position.bottom,
            enableDataLabels = true
        )
    }

    private fun createSummary(content: String, description: String, icon: Icon): Component {
        icon.apply {
            setSize("32px")
            element.style.set("padding", "0 48px 0 0")
        }
        val contents = FlexLayout(
            createTextBox(content, 24, "bold"),
            createTextBox(description, 14)
        ).apply {
            setFlexDirection(FlexDirection.COLUMN)
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
        return createCard(icon, contents).apply {
            alignItems = FlexComponent.Alignment.CENTER
        }
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        add(createTitle(), createContent())
    }
}
