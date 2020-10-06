package support.views

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer
import com.vaadin.flow.data.renderer.Renderer
import java.time.LocalDate
import java.time.LocalDateTime

fun <T : Any> Grid<T>.addNormalColumn(
    renderer: Renderer<T>
): Grid.Column<T> {
    return addColumn(renderer)
}

fun <T : Any> Grid<T>.addNormalColumn(
    labelText: String,
    valueProvider: (T) -> Any
): Grid.Column<T> {
    return addColumn(valueProvider).apply {
        addHeader(labelText)
    }
}

fun <T : Any> Grid<T>.addInMemorySortableColumn(
    labelText: String,
    valueProvider: (T) -> Any
): Grid.Column<T> {
    return addColumn(valueProvider).apply {
        addSortableHeader(labelText)
    }
}

fun <T : Any> Grid<T>.addInMemorySortableDateTimeColumn(
    labelText: String,
    valueProvider: (T) -> LocalDateTime?
): Grid.Column<T> {
    return addColumn(createLocalDateTimeRenderer<T>(valueProvider)).apply {
        addSortableHeader(labelText)
        setComparator(compareBy(valueProvider))
    }
}

fun <T : Any> Grid<T>.addInMemorySortableDateColumn(
    labelText: String,
    valueProvider: (T) -> LocalDate?
): Grid.Column<T> {
    return addColumn(createLocalDateRenderer<T>(valueProvider)).apply {
        addSortableHeader(labelText)
        setComparator(compareBy(valueProvider))
    }
}

fun <T : Any> Grid<T>.addBackEndSortableColumn(
    labelText: String,
    propertyName: String,
    valueProvider: (T) -> Any
): Grid.Column<T> {
    return addColumn(valueProvider).apply {
        addHeader(labelText)
        setSortProperty(propertyName)
    }
}

fun <T : Any> Grid<T>.addBackEndSortableDateTimeColumn(
    labelText: String,
    propertyName: String,
    valueProvider: (T) -> LocalDateTime?
): Grid.Column<T> {
    return addColumn(createLocalDateTimeRenderer<T>(valueProvider)).apply {
        addHeader(labelText)
        setSortProperty(propertyName)
    }
}

fun <T : Any> Grid<T>.addBackEndSortableDateColumn(
    labelText: String,
    propertyName: String,
    valueProvider: (T) -> LocalDate?
): Grid.Column<T> {
    return addColumn(createLocalDateRenderer<T>(valueProvider)).apply {
        addHeader(labelText)
        setSortProperty(propertyName)
    }
}

private fun <T : Any> Grid.Column<T>.addSortableHeader(labelText: String) {
    addHeader(labelText)
    isSortable = true
}

private fun <T : Any> Grid.Column<T>.addHeader(labelText: String) {
    setHeader(labelText)
    isAutoWidth = true
}

private fun <T : Any> createLocalDateTimeRenderer(valueProvider: (T) -> LocalDateTime?): Renderer<T> =
    LocalDateTimeRenderer(valueProvider, "yyyy-MM-dd HH:mm:ss")

private fun <T : Any> createLocalDateRenderer(valueProvider: (T) -> LocalDate?): Renderer<T> =
    LocalDateRenderer(valueProvider, "yyyy-MM-dd")

fun List<QuerySortOrder>.toMap(): Map<String, String> {
    return associate { it.sorted to it.direction.name }
}
