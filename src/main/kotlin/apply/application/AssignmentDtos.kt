package apply.application

import apply.domain.assignment.Assignment
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class AssignmentRequest(
    @field:Pattern(
        regexp = "^[a-z\\d](?:[a-z\\d]|-(?=[a-z\\d])){0,38}",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "올바른 형식의 이름이어야 합니다"
    )
    val githubUsername: String,

    @field:Pattern(
        regexp = "https://github\\.com(/[\\w\\-]+){2}/pull/[1-9]\\d*",
        message = "올바른 형식의 URL이어야 합니다"
    )
    val pullRequestUrl: String,

    @field:NotBlank
    val note: String
)

data class AssignmentData(
    val githubUsername: String,
    val pullRequestUrl: String,
    val note: String
) {
    constructor(assignment: Assignment?) : this(
        assignment?.githubUsername.orEmpty(),
        assignment?.pullRequestUrl.orEmpty(),
        assignment?.note.orEmpty()
    )
}

data class AssignmentResponse(
    val githubUsername: String,
    val pullRequestUrl: String,
    val note: String
)
