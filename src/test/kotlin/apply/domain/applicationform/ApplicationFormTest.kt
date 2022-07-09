package apply.domain.applicationform

import apply.createApplicationForm
import apply.createApplicationFormAnswers
import apply.fail
import apply.pass
import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

internal class ApplicationFormTest : AnnotationSpec() {
    @Test
    fun `지원서가 지원 정책을 충족하는 경우 생성한다`() {
        assertDoesNotThrow {
            ApplicationForm(1L, 1L, pass)
        }
    }

    @Test
    fun `지원서가 지원 정책에 맞지 않으면 생성할 수 없다`() {
        assertThrows<DuplicateApplicationException> {
            ApplicationForm(1L, 1L, fail)
        }
    }

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
    fun `포트폴리오 없이 지원서를 수정한다`() {
        val applicationForm = createApplicationForm()
        applicationForm.update(referenceUrl = "", createApplicationFormAnswers())
        assertThat(applicationForm.referenceUrl).isEmpty()
    }

    @Test
    fun `잘못된 포트폴리오 주소로 지원서를 수정할 수 없다`() {
        val applicationForm = createApplicationForm()
        assertThrows<IllegalArgumentException> {
            applicationForm.update(referenceUrl = "wrong", createApplicationFormAnswers())
        }
    }

    @Test
    fun `지원서를 제출한다`() {
        val applicationForm = createApplicationForm().apply { submit(pass) }

        assertThat(applicationForm.submitted).isTrue()
        assertThat(applicationForm.submittedDateTime).isNotNull()
    }

    @Test
    fun `제출한 지원서를 수정할 수 없다`() {
        val applicationForm = createApplicationForm().apply { submit(pass) }

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
