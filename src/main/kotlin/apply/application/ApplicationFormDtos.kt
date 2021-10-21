package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime

data class CreateApplicationFormRequest(
    val recruitmentId: Long
)

data class UpdateApplicationFormRequest(
    val recruitmentId: Long,

    @field:URL(regexp = ".{0,255}|", message = "공백이거나 올바른 URL이어야 합니다")
    val referenceUrl: String = "",
    val submitted: Boolean = false,
    val answers: List<AnswerRequest> = emptyList()
)

data class AnswerRequest(
    val contents: String,
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
    constructor(applicationFormAnswer: ApplicationFormAnswer) : this(
        applicationFormAnswer.contents,
        applicationFormAnswer.recruitmentItemId
    )
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
