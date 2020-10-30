package apply.ui.admin.dashboard

import apply.application.ApplicantService
import apply.application.RecruitmentService
import apply.ui.admin.BaseLayout
import com.github.appreciated.apexcharts.ApexChartsBuilder
import com.github.appreciated.apexcharts.config.builder.ChartBuilder
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder
import com.github.appreciated.apexcharts.config.builder.TitleSubtitleBuilder
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder
import com.github.appreciated.apexcharts.config.builder.YAxisBuilder
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.config.xaxis.XAxisType
import com.github.appreciated.apexcharts.helper.Series
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEvent
import com.vaadin.flow.router.HasUrlParameter
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.WildcardParameter

@Route(value = "admin/dashboard", layout = BaseLayout::class)
class DashBoardView(
    private val recruitmentService: RecruitmentService,
    private val applicantService: ApplicantService
) : VerticalLayout(), HasUrlParameter<Long> {
    private var recruitmentId = 0L

    private fun createTitle(): Component {
        return HorizontalLayout(H1(recruitmentService.getById(recruitmentId).title)).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createContent(): Component {
        return HorizontalLayout(createTotalApplicant(), createYearApplicant()).apply {
            setWidthFull()
            justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        }
    }

    private fun createTotalApplicant(): Component {
        val applicants = applicantService.findAllByRecruitmentId(recruitmentId)
        return Div(H3("총 지원자(임시저장)"), H3("${applicants.count()}명"))
    }

    private fun createYearApplicant(): Component {
        val applicants = applicantService.findAllByRecruitmentId(recruitmentId)
            .groupingBy { it.birthday.year.toString() }.eachCount()

        return ApexChartsBuilder().apply {
            withChart(ChartBuilder.get().withType(Type.bar).build())
            withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
            withSeries(Series("지원자(임시저장 포함)", *applicants.values.toTypedArray()))
            withTitle(TitleSubtitleBuilder.get().withText("지원자(임시저장 포함)").build())
            withLabels(*applicants.keys.toTypedArray())
            withXaxis(XAxisBuilder.get().withType(XAxisType.categories).build())
            withYaxis(YAxisBuilder.get().withMax(applicants.values.max()?.plus(10)).build())
        }.build()
    }

    override fun setParameter(event: BeforeEvent, @WildcardParameter parameter: Long) {
        this.recruitmentId = parameter
        add(
            createTitle(),
            createContent()
        )
    }
}
