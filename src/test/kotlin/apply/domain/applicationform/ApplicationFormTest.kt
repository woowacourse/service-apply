package apply.domain.applicationform

import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicationFormTest {
    private lateinit var applicationForm: ApplicationForm

    @BeforeEach
    internal fun setUp() {
        applicationForm = ApplicationForm(
            applicantId = 1L,
            recruitmentId = 1L,
            referenceUrl = "http://example.com",
            answers = Answers(
                mutableListOf(
                    Answer(
                        "스타트업을 하고 싶습니다.",
                        1L
                    ),
                    Answer(
                        "책임감",
                        2L
                    )
                )
            )
        )
    }

    @Test
    internal fun saveApplicationFormTest() {
        assertThat(applicationForm.submitted).isFalse()
        assertThat(applicationForm.applicantId).isEqualTo(1L)
        assertThat(applicationForm.referenceUrl).isEqualTo("http://example.com")
        assertThat(applicationForm.answers.items[0].contents).isEqualTo("스타트업을 하고 싶습니다.")
        assertThat(applicationForm.answers.items[1].contents).isEqualTo("책임감")
    }

    @Test
    internal fun updateApplicationFormTest() {
        applicationForm.update(
            "http://h2f.kr",
            Answers(
                mutableListOf(
                    Answer(
                        "대기업에 취직하고 싶습니다.",
                        1L
                    ),
                    Answer(
                        "책임감",
                        2L
                    )
                )
            )
        )
        assertThat(applicationForm.referenceUrl).isEqualTo("http://h2f.kr")
        assertThat(applicationForm.answers.items[0].contents).isEqualTo("대기업에 취직하고 싶습니다.")
    }

    @Test
    internal fun submitApplicationFormTest() {
        applicationForm.submit()

        assertThat(applicationForm.submitted).isTrue()

        assertThatThrownBy {
            applicationForm.update(
                "http://h2f.kr",
                Answers(
                    mutableListOf(
                        Answer(
                            "스타트업을 하고 싶습니다.",
                            1L
                        ),
                        Answer(
                            "책임감",
                            2L
                        )
                    )
                )
            )
        }
            .isInstanceOf(IllegalAccessException::class.java)
            .hasMessageContaining("이미 제출된")
    }
}
