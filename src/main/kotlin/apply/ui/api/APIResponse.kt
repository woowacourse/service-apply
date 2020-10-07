package apply.ui.api

class APIResponse<T>(
    val success: Boolean = true,
    val message: String? = "",
    val body: T? = null
)
