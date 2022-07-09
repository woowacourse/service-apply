package apply.domain.applicationform

import apply.createApplicationForm
import apply.createApplicationFormAnswers
import apply.fail
import apply.pass
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty

internal class ApplicationFormTest : AnnotationSpec() {
    @Test
    fun `지원서가 지원 정책을 충족하는 경우 생성한다`() {
        shouldNotThrow<Exception> { ApplicationForm(1L, 1L, pass) }
    }

    @Test
    fun `지원서가 지원 정책에 맞지 않으면 생성할 수 없다`() {
        shouldThrow<DuplicateApplicationException> { ApplicationForm(1L, 1L, fail) }
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
        assertSoftly {
            applicationForm.referenceUrl shouldBe "https://example2.com"
            applicationForm.answers.items[0].contents shouldBe "수정된 답변 1"
        }
    }

    @Test
    fun `포트폴리오 없이 지원서를 수정한다`() {
        val applicationForm = createApplicationForm()
        applicationForm.update(referenceUrl = "", createApplicationFormAnswers())
        applicationForm.referenceUrl.shouldBeEmpty()
    }

    @Test
    fun `잘못된 포트폴리오 주소로 지원서를 수정할 수 없다`() {
        val applicationForm = createApplicationForm()
        shouldThrow<IllegalArgumentException> {
            applicationForm.update(
                referenceUrl = "wrong",
                createApplicationFormAnswers()
            )
        }
    }

    @Test
    fun `지원서를 제출한다`() {
        val applicationForm = createApplicationForm().apply { submit(pass) }
        assertSoftly {
            applicationForm.submitted.shouldBeTrue()
            applicationForm.submittedDateTime.shouldNotBeNull()
        }
    }

    @Test
    fun `제출한 지원서를 수정할 수 없다`() {
        val applicationForm = createApplicationForm().apply { submit(pass) }
        shouldThrow<IllegalStateException> {
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
