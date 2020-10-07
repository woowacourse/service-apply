package apply.ui.api

class ApiResponse<T>(
    val success: Boolean = true,
    val message: String? = "",
    val body: T? = null
)
