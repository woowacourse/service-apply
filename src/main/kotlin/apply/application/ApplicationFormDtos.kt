package apply.application

import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class SaveApplicationFormRequest(
    val recruitmentId: Long,

    @field:Size(min = 0, max = 255)
    val referenceUrl: String,

    val isSubmitted: Boolean = false,

    val answers: List<AnswerRequest> = emptyList()
)

data class UpdateApplicationFormRequest(
    val recruitmentId: Long,

    @field:Size(min = 0, max = 255)
    val referenceUrl: String,

    val isSubmitted: Boolean = false,

    val answers: List<AnswerRequest> = emptyList(),

    @field:NotBlank
    val password: String
)

data class AnswerRequest(
    @field:Size(min = 1)
    val contents: String,

    @field:NotNull
    val recruitmentItemId: Long
)

data class ApplicationFormResponse(
    val id: Long,
    val recruitmentId: Long,
    val referenceUrl: String,
    val submitted: Boolean,
    val answers: List<AnswerResponse>,
    val createdDateTime: LocalDateTime,
    val modifiedDateTime: LocalDateTime,
    val submittedDateTime: LocalDateTime?
)

data class AnswerResponse(
    val contents: String,
    val recruitmentItemId: Long
)
