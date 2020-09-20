package support.views

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer
import java.time.LocalDate
import java.time.LocalDateTime

fun <T : Any> Grid<T>.addSortableColumn(labelText: String, valueProvider: (T) -> Any): Grid.Column<T> {
    return addColumn(valueProvider).apply {
        addSortableHeader(labelText)
    }
}

fun <T : Any> Grid<T>.addSortableDateTimeColumn(
    labelText: String,
    valueProvider: (T) -> LocalDateTime
): Grid.Column<T> {
    return addColumn(LocalDateTimeRenderer(valueProvider, "yyyy-MM-dd HH:mm:ss")).apply {
        addSortableHeader(labelText)
        setComparator(compareBy(valueProvider))
    }
}

fun <T : Any> Grid<T>.addSortableDateColumn(
    labelText: String,
    valueProvider: (T) -> LocalDate
): Grid.Column<T> {
    return addColumn(LocalDateRenderer(valueProvider, "yyyy-MM-dd")).apply {
        addSortableHeader(labelText)
        setComparator(compareBy(valueProvider))
    }
}

private fun <T : Any> Grid.Column<T>.addSortableHeader(labelText: String) {
    setHeader(labelText)
    isSortable = true
    isAutoWidth = true
}
