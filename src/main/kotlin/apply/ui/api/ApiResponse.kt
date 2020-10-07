package apply.ui.api

data class ApiResponse<T>(
    val message: String? = "",
    val body: T? = null
)
