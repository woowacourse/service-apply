package apply.application

import apply.domain.assignment.Assignment
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AssignmentRequest(
    @field:NotBlank
    val githubUsername: String,

    @field:Size(min = 1, max = 255)
    val pullRequestUrl: String,

    @field:NotBlank
    val note: String
)

data class AssignmentData(
    @field:NotBlank
    val githubUsername: String?,

    @field:Size(min = 1, max = 255)
    val pullRequestUrl: String?,

    @field:NotBlank
    val note: String?
) {
    constructor(assignment: Assignment?) : this(
        assignment?.githubUsername,
        assignment?.pullRequestUrl,
        assignment?.note
    )
}
