package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.recruitmentitem.Answer
import java.time.LocalDateTime
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateApplicationFormRequest(
    @field:NotNull
    val recruitmentId: Long
)

data class UpdateApplicationFormRequest(
    @field:NotNull
    val recruitmentId: Long,

    @field:Size(min = 0, max = 255)
    val referenceUrl: String = "",

    val isSubmitted: Boolean = false,

    val answers: List<AnswerRequest> = emptyList()
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
) {
    constructor(applicationForm: ApplicationForm) : this(
        applicationForm.id,
        applicationForm.recruitmentId,
        applicationForm.referenceUrl,
        applicationForm.submitted,
        applicationForm.answers.items.map(::AnswerResponse),
        applicationForm.createdDateTime,
        applicationForm.modifiedDateTime,
        applicationForm.submittedDateTime
    )
}

data class AnswerResponse(
    val contents: String,
    val recruitmentItemId: Long
) {
    constructor(answer: Answer) : this(answer.contents, answer.recruitmentItemId)
}

data class MyApplicationFormResponse(
    val recruitmentId: Long,
    val submitted: Boolean
) {
    constructor(applicationForm: ApplicationForm) : this(
        applicationForm.recruitmentId,
        applicationForm.submitted
    )
}
