package apply.application

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class AssignmentDtos

data class CreateAssignmentRequest(
    @field:NotNull
    val githubId: String,

    @field:Size(min = 0, max = 255)
    val url: String = "",

    @field:NotBlank
    var impression: String = ""
)
