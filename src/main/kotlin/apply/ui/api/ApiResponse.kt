package apply.ui.api

class ApiResponse<T>(
    val message: String? = "",
    val body: T? = null
)
