package apply.domain.assignment

@JvmInline
value class Url(val value: String) {
    init {
        require(ASSIGNMENT_URL_PATTERN.matches(value)) { "URL 형식이 올바르지 않습니다." }
    }

    companion object {
        private val ASSIGNMENT_URL_PATTERN: Regex = """https://github\.com(/[\w\-]+){2}(/pull/[1-9]\d*)?""".toRegex()
    }
}
