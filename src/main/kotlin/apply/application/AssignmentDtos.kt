package apply.application

import apply.domain.assignment.Assignment
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class AssignmentRequest(
    @field:Pattern(
        regexp = "https://github\\.com(/[\\w\\-]+){2}/pull/[1-9]\\d*",
        message = "올바른 형식의 Pull Request URL이어야 합니다"
    )
    val pullRequestUrl: String,

    @field:Size(max = 5000)
    @field:NotBlank
    val note: String
)

data class AssignmentData(
    val url: String,
    val note: String,
    val id: Long
) {
    constructor(assignment: Assignment?) : this(
        assignment?.url.orEmpty(),
        assignment?.note.orEmpty(),
        assignment?.id ?: 0L
    )
}

data class AssignmentResponse(
    val id: Long,
    val pullRequestUrl: String,
    val note: String
) {
    constructor(assignment: Assignment) : this(
        assignment.id,
        assignment.url,
        assignment.note
    )
}
