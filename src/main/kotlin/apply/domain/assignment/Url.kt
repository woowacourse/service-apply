package apply.domain.assignment

import apply.domain.mission.SubmissionMethod

@JvmInline
value class Url private constructor(val value: String) {
    init {
        require(ASSIGNMENT_URL_PATTERNS.values.any { it.matches(value) }) { "URL 형식이 올바르지 않습니다." }
    }

    companion object {
        private val PULL_REQUEST_URL_PATTERN: Regex = """https://github\.com(/[\w\-]+){2}/pull/[1-9]\d*""".toRegex()
        private val REPOSITORY_URL_PATTERN: Regex = """https://github\.com(/[\w\-]+){2}""".toRegex()
        private val ASSIGNMENT_URL_PATTERNS: Map<SubmissionMethod, Regex> = mapOf(
            SubmissionMethod.PUBLIC_PULL_REQUEST to PULL_REQUEST_URL_PATTERN,
            SubmissionMethod.PRIVATE_REPOSITORY to REPOSITORY_URL_PATTERN
        )

        // TODO: 코틀린 1.9에서 부 생성자로 변경
        fun of(value: String, submissionMethod: SubmissionMethod): Url {
            require(ASSIGNMENT_URL_PATTERNS.getValue(submissionMethod).matches(value)) { "URL 형식이 올바르지 않습니다." }
            return Url(value)
        }
    }
}
