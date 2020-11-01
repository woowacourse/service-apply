package apply.ui.admin.dashboard

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.RecruitmentService
import apply.domain.applicant.Gender
import apply.ui.admin.BaseLayout
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.helper.Series
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import support.views.Colors
import support.views.createCard
import support.views.createChart
import support.views.createHorizontalDivider
import support.views.createTextBox
import java.util.SortedMap

@Route(value = "admin", layout = BaseLayout::class)
class MainView(
    private val recruitmentService: RecruitmentService,
    private val applicantService: ApplicantService
) : FlexLayout() {
    init {
        recruitmentService.findAll()
            .forEach {
                add(createTitle(it.id), createContent(it.id))
            }
        setFlexDirection(FlexDirection.COLUMN)
        element.style.set("background-color", "#f1f2f6")
    }

    private fun createTitle(recruitmentId: Long): Component {
        return FlexLayout(
            createHorizontalDivider(1),
            H2(recruitmentService.getById(recruitmentId).title).apply {
                element.style.set("margin", "0 5px")
                element.style.set("color", Colors.PRIMARY.color)
            },
            createHorizontalDivider(5)
        ).apply {
            element.style.set("margin", "20px 0")
            alignItems = FlexComponent.Alignment.CENTER
        }
    }

    private fun createContent(recruitmentId: Long): Component {
        val totalApplicants = applicantService.findAllByRecruitmentId(recruitmentId)
        val submittedApplicants = totalApplicants.filter { it.applicationForm.submitted }

        return FlexLayout(
            FlexLayout(
                createSummary("${totalApplicants.count()}명", "지원자", Icon(VaadinIcon.TAG), Colors.SKYBLUE)
                    .apply { element.style.set("flex", "1") },
                createSummary("${submittedApplicants.count()}명", "제출자", Icon(VaadinIcon.TAG), Colors.PINK)
                    .apply { element.style.set("flex", "1") }
            ),
            createStatistics(totalApplicants, submittedApplicants)
        ).apply {
            setFlexDirection(FlexDirection.COLUMN)
        }
    }

    private fun createStatistics(
        totalApplicants: List<ApplicantAndFormResponse>,
        submittedApplicants: List<ApplicantAndFormResponse>
    ): FlexLayout {
        return FlexLayout(
            createDateApplicant(totalApplicants, submittedApplicants).apply { element.style.set("flex", "1") },
            createYearApplicant(totalApplicants, submittedApplicants).apply { element.style.set("flex", "1") },
            createGenderApplicant(totalApplicants, submittedApplicants).apply { element.style.set("flex", "1") }
        ).apply {
            setWidthFull()
        }
    }

    private fun createDateApplicant(
        totalApplicants: List<ApplicantAndFormResponse>,
        submittedApplicants: List<ApplicantAndFormResponse>
    ): Component {
        val totalDateGroup = totalApplicants.countMap { it.applicationForm.createdDateTime.toLocalDate().toString() }
        val submittedDateGroup =
            submittedApplicants.countMap { it.applicationForm.submittedDateTime!!.toLocalDate().toString() }

        totalDateGroup.keys.forEach {
            submittedDateGroup.putIfAbsent(it, 0)
        }

        return createChart(
            title = "날짜",
            type = Type.line,
            labels = submittedDateGroup.keys,
            series = listOf(
                Series("총 지원자", *totalDateGroup.values.toTypedArray()),
                Series("제출자", *submittedDateGroup.values.toTypedArray())
            )
        )
    }

    private fun createYearApplicant(
        totalApplicants: List<ApplicantAndFormResponse>,
        submittedApplicants: List<ApplicantAndFormResponse>
    ): Component {
        val totalBirthdayGroup = totalApplicants.countMap { it.birthday.year.toString() }
        val submittedBirthdayGroup = submittedApplicants.countMap { it.birthday.year.toString() }

        totalBirthdayGroup.keys.forEach {
            submittedBirthdayGroup.putIfAbsent(it, 0)
        }

        return createChart(
            title = "나이",
            type = Type.bar,
            labels = totalBirthdayGroup.keys,
            series = listOf(
                Series("지원자", *totalBirthdayGroup.values.toTypedArray()),
                Series("제출자", *submittedBirthdayGroup.values.toTypedArray())
            )
        )
    }

    private fun createGenderApplicant(
        totalApplicants: List<ApplicantAndFormResponse>,
        submittedApplicants: List<ApplicantAndFormResponse>
    ): Component {
        val totalGenderGroup = totalApplicants.countMap { it.gender.title }
        val submittedGenderGroup = submittedApplicants.countMap { it.gender.title }

        return createChart(
            title = "성비",
            type = Type.bar,
            labels = listOf("지원자", "제출자"),
            series = listOf(
                Series("남", totalGenderGroup[Gender.MALE.title], submittedGenderGroup[Gender.MALE.title]),
                Series("여", totalGenderGroup[Gender.FEMALE.title], submittedGenderGroup[Gender.FEMALE.title])
            )
        )
    }

    private fun createSummary(content: String, description: String, icon: Icon, color: Colors): Component {
        icon.apply {
            setSize("32px")
            setColor(color.color)
            element.style.set("padding", "0 48px 0 0")
        }
        val contents = FlexLayout(
            createTextBox(content, 24, "bold", color),
            createTextBox(description, 14)
        ).apply {
            setFlexDirection(FlexDirection.COLUMN)
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
        return createCard(icon, contents).apply {
            alignItems = FlexComponent.Alignment.CENTER
        }
    }

    private fun List<ApplicantAndFormResponse>.countMap(keySelector: (ApplicantAndFormResponse) -> String): SortedMap<String, Int> {
        return groupingBy { keySelector(it) }.eachCount().toSortedMap()
    }
}
