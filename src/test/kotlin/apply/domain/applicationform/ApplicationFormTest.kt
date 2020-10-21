package apply.domain.applicationform

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class ApplicationFormTest {
    private lateinit var applicationForm: ApplicationForm

    @BeforeEach
    internal fun setUp() {
        applicationForm = ApplicationForm(
            applicantId = 1L,
            recruitmentId = 1L,
            referenceUrl = "http://example.com",
            answers = ApplicationFormAnswers(
                mutableListOf(
                    ApplicationFormAnswer(
                        "스타트업을 하고 싶습니다.",
                        1L
                    ),
                    ApplicationFormAnswer(
                        "책임감",
                        2L
                    )
                )
            )
        )
    }

    @Test
    fun `지원서가 잘 만들어졌는지 테스트한다`() {
        assertAll(
            { assertThat(applicationForm.submitted).isFalse() },
            { assertThat(applicationForm.applicantId).isEqualTo(1L) },
            { assertThat(applicationForm.referenceUrl).isEqualTo("http://example.com") },
            { assertThat(applicationForm.answers.items[0].contents).isEqualTo("스타트업을 하고 싶습니다.") },
            { assertThat(applicationForm.answers.items[1].contents).isEqualTo("책임감") }
        )
    }

    @Test
    fun `지원서의 업데이트를 테스트한다`() {
        applicationForm.update(
            "http://h2f.kr",
            ApplicationFormAnswers(
                mutableListOf(
                    ApplicationFormAnswer(
                        "대기업에 취직하고 싶습니다.",
                        1L
                    ),
                    ApplicationFormAnswer(
                        "책임감",
                        2L
                    )
                )
            )
        )
        assertAll(
            { assertThat(applicationForm.referenceUrl).isEqualTo("http://h2f.kr") },
            { assertThat(applicationForm.answers.items[0].contents).isEqualTo("대기업에 취직하고 싶습니다.") }
        )
    }

    @Test
    fun `지원서의 제출을 테스트한다`() {
        applicationForm.submit()

        assertThat(applicationForm.submitted).isTrue()

        assertThatIllegalStateException().isThrownBy {
            applicationForm.update(
                "http://h2f.kr",
                ApplicationFormAnswers(
                    mutableListOf(
                        ApplicationFormAnswer(
                            "스타트업을 하고 싶습니다.",
                            1L
                        ),
                        ApplicationFormAnswer(
                            "책임감",
                            2L
                        )
                    )
                )
            )
        }
            .withMessageContaining("이미 제출된")
    }
}
