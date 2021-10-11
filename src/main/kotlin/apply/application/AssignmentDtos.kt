package apply.application

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

// TODO: 추가 data class 추가 시 interface 제거
interface AssignmentDtos

data class AssignmentData(
    @field:NotBlank
    val githubUsername: String,

    @field:Size(min = 1, max = 255)
    val pullRequestUrl: String,

    @field:NotBlank
    val note: String
)
