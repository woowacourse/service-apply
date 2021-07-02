package support

import org.springframework.data.domain.Sort

fun Map<String, String>.toSort(): Sort {
    return Sort.by(
        filterKeys { it.isNotBlank() }
            .map { Sort.Order(it.value.toDirection(), it.key) }
    )
}

private fun String.toDirection(): Sort.Direction {
    return when (this.toUpperCase()) {
        "ASCENDING" -> Sort.Direction.ASC
        "DESCENDING" -> Sort.Direction.DESC
        else -> Sort.Direction.fromString(this)
    }
}
