package apply.application

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

data class AssignmentResponse(
    val githubUsername: String,
    val pullRequestUrl: String,
    val note: String
)
