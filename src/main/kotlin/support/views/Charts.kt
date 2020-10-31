package support.views

import com.github.appreciated.apexcharts.ApexCharts
import com.github.appreciated.apexcharts.config.Chart
import com.github.appreciated.apexcharts.config.DataLabels
import com.github.appreciated.apexcharts.config.Legend
import com.github.appreciated.apexcharts.config.chart.Toolbar
import com.github.appreciated.apexcharts.config.chart.Type
import com.github.appreciated.apexcharts.config.legend.Position
import com.github.appreciated.apexcharts.helper.Series
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.FlexLayout

fun createChart(
    title: String,
    type: Type,
    series: List<Double>,
    labels: Set<String>,
    legendPosition: Position = Position.right,
    showToolbar: Boolean = false,
    enableDataLabels: Boolean = false
): FlexLayout {
    return createCard(
        createTextBox(title, 24, "bold"),
        createChart(type, labels, legendPosition, showToolbar, enableDataLabels)
            .apply { setSeries(*series.toTypedArray()) },
        direction = FlexLayout.FlexDirection.COLUMN
    ).apply {
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
    }
}

fun createChart(
    title: String,
    type: Type,
    series: Series<*>,
    labels: Set<String>,
    legendPosition: Position = Position.right,
    showToolbar: Boolean = false,
    enableDataLabels: Boolean = false
): FlexLayout {
    return createCard(
        createTextBox(title, 24, "bold"),
        createChart(type, labels, legendPosition, showToolbar, enableDataLabels)
            .apply { setSeries(series) },
        direction = FlexLayout.FlexDirection.COLUMN
    ).apply {
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER
    }
}

private fun createChart(
    type: Type,
    labels: Set<String>,
    legendPosition: Position,
    showToolbar: Boolean,
    enableDataLabels: Boolean
): ApexCharts {
    return ApexCharts().apply {
        setChart(
            Chart().apply {
                this.type = type
                this.toolbar = Toolbar().apply { show = showToolbar }
            }
        )
        setDataLabels(DataLabels().apply { enabled = enableDataLabels })
        setLabels(*labels.toTypedArray())
        setLegend(Legend().apply { position = legendPosition })
    }.apply {
        width = "480px"
        height = "480px"
        element.style.set("display", "flex")
        element.style.set("justify-content", "center")
        element.style.set("align-items", "center")
    }
}
