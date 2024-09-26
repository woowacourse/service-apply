package apply.application

import apply.domain.assignment.Assignment
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class AssignmentRequest(
    val url: String,

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
    val url: String,
    val note: String
) {
    constructor(assignment: Assignment) : this(
        assignment.id,
        assignment.url,
        assignment.note
    )
}
