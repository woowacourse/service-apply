package apply.domain.applicationform

import apply.createApplicationForm
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

internal class ApplicationFormTest {
    @Test
    fun `지원서를 수정한다`() {
        val applicationForm = createApplicationForm(referenceUrl = "https://example.com").apply {
            update(
                referenceUrl = "https://example2.com",
                applicationFormAnswers = ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer("수정된 답변 1", 1L)
                    )
                )
            )
        }

        assertAll(
            { assertThat(applicationForm.referenceUrl).isEqualTo("https://example2.com") },
            { assertThat(applicationForm.answers.items[0].contents).isEqualTo("수정된 답변 1") }
        )
    }

    @Test
    fun `지원서를 제출한다`() {
        val applicationForm = createApplicationForm().apply { submit() }

        assertThat(applicationForm.submitted).isTrue()
        assertThat(applicationForm.submittedDateTime).isNotNull()
    }

    @Test
    fun `제출한 지원서를 수정할 수 없다`() {
        val applicationForm = createApplicationForm().apply { submit() }

        assertThrows<IllegalStateException> {
            applicationForm.update(
                referenceUrl = "https://example2.com",
                applicationFormAnswers = ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer("수정된 답변 1", 1L)
                    )
                )
            )
        }
    }
}
