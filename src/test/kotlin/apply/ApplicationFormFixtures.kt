package apply

import apply.application.AnswerRequest
import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import apply.domain.applicationform.ApplicationFormAnswers

fun createApplicationForm(
    applicantId: Long = 1L,
    recruitmentId: Long = 1L,
    referenceUrl: String = "http://example.com",
    applicationFormAnswers: ApplicationFormAnswers = createApplicationFormAnswers()
): ApplicationForm {
    return ApplicationForm(applicantId, recruitmentId, referenceUrl, applicationFormAnswers)
}

fun createApplicationFormAnswers(
    applicationFormAnswers: MutableList<ApplicationFormAnswer> = mutableListOf(
        createApplicationFormAnswer(),
        createApplicationFormAnswer("책임감", 2L)
    )
): ApplicationFormAnswers {
    return ApplicationFormAnswers(applicationFormAnswers)
}

fun createApplicationFormAnswer(
    contents: String = "스타트업을 하고 싶습니다.",
    recruitmentItemId: Long = 1L
): ApplicationFormAnswer {
    return ApplicationFormAnswer(contents, recruitmentItemId)
}

fun createAnswerRequest(
    contents: String = "책임감",
    recruitmentItemId: Long = 1L
): AnswerRequest {
    return AnswerRequest(contents, recruitmentItemId)
}

fun createExceededAnswerRequest(
    contents: String = "*".repeat(MAXIMUM_LENGTH + 1),
    recruitmentItemId: Long = 1L
): AnswerRequest {
    return AnswerRequest(contents, recruitmentItemId)
}

fun createApplicationForms(
    applicationForm1: ApplicationForm = ApplicationForm(
        applicantId = 1L,
        recruitmentId = 1L,
        referenceUrl = "http://example.com",
        answers = ApplicationFormAnswers(
            mutableListOf(
                ApplicationFormAnswer("스타트업을 하고 싶습니다.", 1L),
                ApplicationFormAnswer("책임감", 2L)
            )
        )
    ),
    applicationForm2: ApplicationForm = ApplicationForm(
        applicantId = 1L,
        recruitmentId = 2L,
        referenceUrl = "http://example2.com",
        answers = ApplicationFormAnswers(
            mutableListOf(
                ApplicationFormAnswer("대기업에 취직하고 싶습니다.", 1L),
                ApplicationFormAnswer("신중함", 2L)
            )
        )
    )
): List<ApplicationForm> {
    return listOf(applicationForm1, applicationForm2)
}
