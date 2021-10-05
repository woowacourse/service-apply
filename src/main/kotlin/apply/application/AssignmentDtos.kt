package apply.application

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class AssignmentDtos

data class CreateAssignmentRequest(
    @field:NotBlank
    val githubUsername: String,

    @field:Size(min = 1, max = 255)
    val pullRequestUrl: String = "",

    @field:NotBlank
    var note: String = ""
)
