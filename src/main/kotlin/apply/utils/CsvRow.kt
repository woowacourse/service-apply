package apply.utils

data class CsvRow(
    val data: List<String>
) {
    constructor(vararg data: String) : this(data.toList())
}
