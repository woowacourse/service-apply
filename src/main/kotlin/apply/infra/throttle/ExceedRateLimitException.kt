package apply.infra.throttle

class ExceedRateLimitException(message: String? = null) : RuntimeException(message)
