package apply.ui.admin.dashboard

import apply.application.ApplicantAndFormResponse
import apply.application.ApplicantService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.config.legend.Position
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
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.SortedMap
import java.util.SortedSet

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
        element.style.set("background-color", Colors.GRAY.color)
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
                createSummary("${submittedApplicants.count()}명", "지원자", Icon(VaadinIcon.TAG), Colors.PINK)
                    .apply { element.style.set("flex", "1") },
                createSummary("${totalApplicants.count()}명", "가제출자", Icon(VaadinIcon.TAG), Colors.SKYBLUE)
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
            createChartBySubmittedDate(totalApplicants, submittedApplicants).apply { element.style.set("flex", "1") },
            createChartByBirthday(submittedApplicants).apply { element.style.set("flex", "1") },
            createChartByGender(submittedApplicants).apply { element.style.set("flex", "1") }
        ).apply {
            setWidthFull()
        }
    }

    private fun createChartBySubmittedDate(
        totalApplicants: List<ApplicantAndFormResponse>,
        submittedApplicants: List<ApplicantAndFormResponse>
    ): Component {
        val totalGroup = totalApplicants.countGroupBy {
            it.applicationForm.createdDateTime.format(DateTimeFormatter.ofPattern("MM-dd"))
        }.accumulate()
        val submittedGroup = submittedApplicants.countGroupBy {
            it.applicationForm.submittedDateTime!!.format(DateTimeFormatter.ofPattern("MM-dd"))
        }.accumulate()

        val labels = (totalGroup.keys + submittedGroup.keys).toSortedSet()

        val totalData = labels.getDataOrLastValueFrom(totalGroup)
        val submittedData = labels.getDataOrLastValueFrom(submittedGroup)

        return createChart(
            title = "날짜",
            type = Type.line,
            labels = labels,
            series = listOf(
                Series("가제출자", *totalData.toTypedArray()),
                Series("지원자", *submittedData.toTypedArray())
            )
        )
    }

    private fun createChartByBirthday(applicants: List<ApplicantAndFormResponse>): Component {
        val birthdayGroup = applicants.countGroupBy {
            it.birthday.toAge().sectionize(5)
        }

        return createChart(
            title = "나이(만)",
            type = Type.bar,
            labels = birthdayGroup.keys,
            series = listOf(Series("지원자", *birthdayGroup.values.toTypedArray()))
        )
    }

    private fun createChartByGender(applicants: List<ApplicantAndFormResponse>): Component {
        val genderGroup = applicants.countGroupBy { it.gender.title }

        return createChart(
            title = "성비",
            type = Type.pie,
            labels = genderGroup.keys,
            series = genderGroup.values.map { it.toDouble() },
            legendPosition = Position.bottom,
            enableDataLabels = true
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

    private fun SortedMap<String, Int>.accumulate(): SortedMap<String, Int> {
        var total = 0
        for (entry in entries) {
            total += entry.value
            put(entry.key, total)
        }
        return this
    }

    private fun SortedSet<String>.getDataOrLastValueFrom(group: SortedMap<String, Int>): List<Int> {
        var lastValue = 0
        return map {
            group.getOrDefault(it, lastValue).apply { lastValue = this }
        }
    }

    private fun LocalDate.toAge(): Int {
        return Period.between(LocalDate.now(), this).years
    }

    private fun Int.sectionize(range: Int): String {
        val group = this / range
        return "${group * range}-${(group + 1) * range - 1}"
    }

    private fun List<ApplicantAndFormResponse>.countGroupBy(keySelector: (ApplicantAndFormResponse) -> String): SortedMap<String, Int> {
        return groupingBy { keySelector(it) }.eachCount().toSortedMap()
    }
}
