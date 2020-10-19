package apply

import apply.domain.applicationform.ApplicationForm
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers

fun createApplicationForm(
    applicantId: Long = 1L,
    recruitmentId: Long = 1L,
    referenceUrl: String = "http://example.com",
    answers: Answers = createAnswers()
): ApplicationForm {
    return ApplicationForm(applicantId, recruitmentId, referenceUrl, answers)
}

fun createAnswers(
    answers: MutableList<Answer> = mutableListOf(
        createAnswer(),
        createAnswer("책임감", 2L)
    )
): Answers {
    return Answers(answers)
}

fun createAnswer(
    contents: String = "스타트업을 하고 싶습니다.",
    recruitmentItemId: Long = 1L
): Answer {
    return Answer(contents, recruitmentItemId)
}

fun createApplicationForms(
    applicationForm1: ApplicationForm = ApplicationForm(
        applicantId = 1L,
        recruitmentId = 1L,
        referenceUrl = "http://example.com",
        answers = Answers(
            mutableListOf(
                Answer("스타트업을 하고 싶습니다.", 1L),
                Answer("책임감", 2L)
            )
        )
    ),
    applicationForm2: ApplicationForm = ApplicationForm(
        applicantId = 1L,
        recruitmentId = 2L,
        referenceUrl = "http://example2.com",
        answers = Answers(
            mutableListOf(
                Answer("대기업에 취직하고 싶습니다.", 1L),
                Answer("신중함", 2L)
            )
        )
    )
): List<ApplicationForm> {
    return listOf(applicationForm1, applicationForm2)
}
